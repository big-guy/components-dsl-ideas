package ng.org.gradle.java;

import ng.org.gradle.java.model.JavaSourceSet;
import ng.org.gradle.jvm.model.JvmFeature;
import ng.org.gradle.software.model.Component;
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

        // Rules that apply to every component
        model.getNgComponents().withType(Component.class).configureEach(component -> {
            component.getFeatures().withType(JvmFeature.class).configureEach(feature -> {
                // all features can have a JavaSourceSet
                feature.getSources().registerBinding(JavaSourceSet.class, JavaSourceSet.class);
                feature.getTargets().configureEach(target -> {
                    // all targets can have a JavaSourceSet
                    target.getSources().registerBinding(JavaSourceSet.class, JavaSourceSet.class);
                });
            });
        });

        // Rules that apply to the "main" component
        model.getNgComponents().withType(Component.class).named(project.getName(), component -> {
            component.getFeatures().withType(JvmFeature.class).configureEach(feature -> {
                // each feature has a JavaSourceSet named java
                feature.getSources().register("java", JavaSourceSet.class);
                feature.getTargets().configureEach(target -> {
                    // each target has a JavaSourceSet named java
                    target.getSources().register("java", JavaSourceSet.class);
                });
            });
        });
    }
}
