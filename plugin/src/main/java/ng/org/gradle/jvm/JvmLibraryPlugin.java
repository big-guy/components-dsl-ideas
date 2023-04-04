package ng.org.gradle.jvm;

import ng.org.gradle.jvm.model.JvmLibraryFeature;
import ng.org.gradle.jvm.model.JvmLibraryTarget;
import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.LibraryComponent;
import ng.org.gradle.software.model.Model;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.stream.Collectors;

/**
 *  jvm-library registers main(JvmLibraryFeature)
 */
public abstract class JvmLibraryPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("ng.org.gradle.software-library");
        Model model = project.getExtensions().getByType(Model.class);

        // Rules apply to all components
        model.getNgComponents().withType(Component.class).configureEach(component -> {
            // Every component can have a JvmLibraryFeature registered
            component.getFeatures().registerBinding(JvmLibraryFeature.class, JvmLibraryFeature.class);

            component.getFeatures().withType(JvmLibraryFeature.class).configureEach(feature -> {
                // Every JvmLibraryFeature can have JvmLibraryTargets registered
                feature.getTargets().registerBinding(JvmLibraryTarget.class, JvmLibraryTarget.class);

                // TODO: How does one override this so that we don't create JvmLibraryTargets?
                // By convention, a JvmLibraryFeature has one JvmLibraryTarget for each target JDK
                feature.getTargets().addAllLater(feature.getTargetJdks().map(jdks -> {
                    return jdks.stream().map(jdk -> {
                        return feature.getTargets().create(feature.getName() + jdk, JvmLibraryTarget.class, target -> {
                            target.getTargetJdk().convention(jdk);
                        });
                    }).collect(Collectors.toList());
                }));
            });
        });

        // Rule only applies to the main component
        model.getNgComponents().withType(LibraryComponent.class).named(project.getName(), component -> {
            component.getFeatures().register("main", JvmLibraryFeature.class);
        });
    }
}
