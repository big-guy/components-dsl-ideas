package ng.org.gradle.jvm;

import ng.org.gradle.jvm.model.JvmLibraryFeature;
import ng.org.gradle.jvm.model.JvmLibraryTarget;
import ng.org.gradle.jvm.model.JvmRuntime;
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
        project.getPluginManager().apply("ng.org.gradle.jvm-base");

        Model model = project.getExtensions().getByType(Model.class);
        // Rules apply to all components
        model.getNgComponents().withType(Component.class).configureEach(component -> {
            component.getFeatures().withType(JvmLibraryFeature.class).configureEach(feature -> {
                // TODO: How does one override this so that we don't create JvmLibraryTargets?
                // By convention, a JvmLibraryFeature has one JvmLibraryTarget for each target JDK
                feature.getTargets().addAllLater(feature.getTargetJdks().map(jdks -> {
                    return jdks.stream().map(jdk -> {
                        return feature.getTargets().create(feature.getName() + jdk, JvmLibraryTarget.class, target -> {
                            target.getTargetJdk().convention(jdk);
                        });
                    }).collect(Collectors.toList());
                }));

                feature.getTargets().configureEach(target -> {
                    target.getVariants().register(target.nameOf("runtimeElements"), JvmRuntime.class);
                });
            });
        });

        // TODO: This shouldn't assume the component name is the project name
        // Maybe?
        /*
        model.getMainComponent().configure(component -> {
            component.getFeatures().register("main", JvmLibraryFeature.class);
        });
         */

        // Rule only applies to the main component
        model.getNgComponents().withType(LibraryComponent.class).named(project.getName(), component -> {
            component.getFeatures().register("main", JvmLibraryFeature.class);
        });

        // TODO: It would be nice to do this lazily...
        // Maybe
        // project.getExtensions().registerAs("library", JvmLibraryFeature.class, model.getMainComponent().flatMap(component -> component.getFeatures().withType(JvmLibraryFeature.class).named("main")));
        JvmLibraryFeature main = model.getNgComponents().getByName(project.getName()).getFeatures().withType(JvmLibraryFeature.class).getByName("main");
        project.getExtensions().add(JvmLibraryFeature.class, "library", main);
    }
}
