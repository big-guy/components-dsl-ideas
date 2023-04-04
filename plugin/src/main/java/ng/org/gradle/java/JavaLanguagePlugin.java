package ng.org.gradle.java;

import ng.org.gradle.java.model.JavaSourceSet;
import ng.org.gradle.jvm.model.JvmFeature;
import ng.org.gradle.jvm.model.JvmLibraryFeature;
import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.LibraryComponent;
import ng.org.gradle.software.model.Model;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * java-language sets up pipeline to take Java sources -> variants?
 */
public abstract class JavaLanguagePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("ng.org.gradle.jvm-ecosystem");
        Model model = project.getExtensions().getByType(Model.class);
        model.getComponents().withType(Component.class).named(project.getName(), component -> {
            component.getFeatures().withType(JvmFeature.class).configureEach(feature -> {
                feature.getSources().registerBinding(JavaSourceSet.class, JavaSourceSet.class);
                feature.getSources().register("java", JavaSourceSet.class);
                feature.getTargets().configureEach(target -> {
                    target.getSources().registerBinding(JavaSourceSet.class, JavaSourceSet.class);
                    target.getSources().register("java", JavaSourceSet.class);
                });
            });
        });
    }
}
