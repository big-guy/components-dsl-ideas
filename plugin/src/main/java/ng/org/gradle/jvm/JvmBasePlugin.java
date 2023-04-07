package ng.org.gradle.jvm;

import ng.org.gradle.jvm.model.JvmFeature;
import ng.org.gradle.jvm.model.JvmLibraryFeature;
import ng.org.gradle.jvm.model.JvmLibraryTarget;
import ng.org.gradle.jvm.model.JvmRuntime;
import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.Model;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public abstract class JvmBasePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("ng.org.gradle.software-component");
        Model model = project.getExtensions().getByType(Model.class);

        // Rules that apply to every component
        model.getNgComponents().withType(Component.class).configureEach(component -> {
            // Every component can have a JvmLibraryFeature registered
            component.getFeatures().registerBinding(JvmLibraryFeature.class, JvmLibraryFeature.class);

            component.getFeatures().withType(JvmLibraryFeature.class).configureEach(feature -> {
                // Every JvmLibraryFeature can have JvmLibraryTargets registered
                feature.getTargets().registerBinding(JvmLibraryTarget.class, JvmLibraryTarget.class);
            });

            component.getFeatures().withType(JvmFeature.class).configureEach(feature -> {
                feature.getTargets().configureEach(target -> {
                    target.getVariants().registerBinding(JvmRuntime.class, JvmRuntime.class);
                });
            });
        });
    }
}
