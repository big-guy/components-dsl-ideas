package ng.org.gradle.java;

import ng.org.gradle.java.model.JavaApi;
import ng.org.gradle.java.model.JavaDocs;
import ng.org.gradle.java.model.JavaSourceSet;
import ng.org.gradle.java.model.JavaSources;
import ng.org.gradle.jvm.model.JvmFeature;
import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.Model;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public abstract class JavaLanguageBasePlugin implements Plugin<Project> {
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

                    target.getVariants().registerBinding(JavaApi.class, JavaApi.class);
                    target.getVariants().registerBinding(JavaDocs.class, JavaDocs.class);
                    target.getVariants().registerBinding(JavaSources.class, JavaSources.class);
                });
            });
        });
    }
}
