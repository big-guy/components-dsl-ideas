package ng.org.gradle.jvm.model;

import org.gradle.api.file.ConfigurableFileCollection;

public interface JvmCompilation extends JvmVariant {
    ConfigurableFileCollection getCompileClasspath();
}
