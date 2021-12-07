/*
 * Copyright 2021 SpotBugs team
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.spotbugs.snom;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import groovy.lang.Closure;
import java.io.File;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.reporting.CustomizableHtmlReport;
import org.gradle.api.reporting.Report;
import org.gradle.api.reporting.SingleFileReport;
import org.gradle.api.resources.TextResource;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.util.ConfigureUtil;

public abstract class SpotBugsReport
    implements SingleFileReport,
        CustomizableHtmlReport // to expose CustomizableHtmlReport#setStylesheet to build script
{
  private final RegularFileProperty destination;
  private final Property<Boolean> isRequired;
  private final SpotBugsTask task;

  @Inject
  public SpotBugsReport(ObjectFactory objects, SpotBugsTask task) {
    this.destination = objects.fileProperty();
    this.isRequired = objects.property(Boolean.class).convention(Boolean.TRUE);
    this.task = task;
  }

  @NonNull
  public abstract String toCommandLineOption();

  /** @deprecated use {@link #getOutputLocation()} instead. */
  @Override
  @Deprecated
  public File getDestination() {
    return destination.get().getAsFile();
  }

  @Override
  public RegularFileProperty getOutputLocation() {
    return destination;
  }

  @Override
  @Internal("This property returns always same value")
  public OutputType getOutputType() {
    return OutputType.FILE;
  }

  @Override
  @Input
  public Property<Boolean> getRequired() {
    return isRequired;
  }

  /** @deprecated use {@link #getRequired()} instead. */
  @Deprecated
  @Override
  public boolean isEnabled() {
    return isRequired.get();
  }

  /** @deprecated use {@code getRequired().set(value)} instead. */
  @Deprecated
  @Override
  public void setEnabled(boolean b) {
    isRequired.set(b);
  }

  /** @deprecated use {@code getRequired().set(provider)} instead. */
  @Deprecated
  @Override
  public void setEnabled(Provider<Boolean> provider) {
    isRequired.set(provider);
  }

  /** @deprecated use {@code getOutputLocation().set(file)} instead. */
  @Deprecated
  @Override
  public void setDestination(File file) {
    destination.set(file);
  }

  /** @deprecated use {@code getOutputLocation().set(provider)} instead. */
  @Deprecated
  @Override
  public void setDestination(Provider<File> provider) {
    destination.set(this.task.getProject().getLayout().file(provider));
  }

  @Override
  public Report configure(Closure closure) {
    ConfigureUtil.configureSelf(closure, this);
    return this;
  }

  @Override
  @Internal("This property provides only a human readable name.")
  public String getDisplayName() {
    return String.format("%s type report generated by the task %s", getName(), getTask().getPath());
  }

  @CheckForNull
  @Override
  // TODO adding an @Input triggers 'cannot be serialized' exception
  public TextResource getStylesheet() {
    return null;
  }

  @Override
  public void setStylesheet(@Nullable TextResource textResource) {
    throw new UnsupportedOperationException(
        String.format("stylesheet property is not available in the %s type report", getName()));
  }

  public void setStylesheet(@Nullable String path) {
    throw new UnsupportedOperationException(
        String.format("stylesheet property is not available in the %s type report", getName()));
  }

  @NonNull
  @Internal
  protected final SpotBugsTask getTask() {
    return task;
  }
}
