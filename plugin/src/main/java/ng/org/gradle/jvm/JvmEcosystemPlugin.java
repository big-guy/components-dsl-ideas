package ng.org.gradle.jvm;

import ng.org.gradle.jvm.model.JvmFeature;
import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.Model;
import org.gradle.api.Named;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.stream.Collectors;

/**
 * jvm-ecosystem registers rules for JavaApi, JavaRuntime, etc variants
 */
public abstract class JvmEcosystemPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("ng.org.gradle.software-component");
        Model model = project.getExtensions().getByType(Model.class);

        // Rules to apply to all components with JvmFeatures
        model.getNgComponents().withType(Component.class).configureEach(component -> {
            component.getFeatures().withType(JvmFeature.class).configureEach(feature -> {
                // All variants for each target are also available at the feature level
                // TODO: Target variant names need to be globally unique
                // TODO: How do we do this more elegantly?
                // TODO: How do we prevent additions to the feature variants list? this should probably be immutable
                feature.getVariants().addAllLater(project.provider(() -> feature.getTargets().stream().flatMap(target -> target.getVariants().stream()).collect(Collectors.toList())));

                // set conventions for source sets in a JVM feature
                feature.getSources().configureEach(sourceSet -> {
                    sourceSet.srcDir("src/" + feature.getName() + "/" + sourceSet.getName());
                });

                // TODO: Should we synchronize the contents of the common source set with the target?
                // TODO: how does this play nicely with addLater or addAllLater?
                // Maybe?
                // Every source set added to feature is also registered on target
                /*
                feature.getSources().whenObjectRegistered(commonSourceSet -> {
                    feature.getTargets().configureEach(target -> {
                        target.getSources().register(commonSourceSet.getName(), commonSourceSet.getType(), sourceSet -> {
                            sourceSet.getSrcDirs().from(commonSourceSet);
                        });
                    });
                });
                 */

                feature.getTargets().configureEach(target -> {
                    target.getDependencies().getImplementationDependencies().addAll(feature.getDependencies().getImplementationDependencies());
                    target.getDependencies().getApiDependencies().addAll(feature.getDependencies().getApiDependencies());

                    target.getSources().configureEach(sourceSet -> {
                        sourceSet.srcDir("src/" + target.getName() + "/" + sourceSet.getName());
                        // TODO: better way to do this above
                        sourceSet.getSrcDirs().from(feature.getSources().matching(other -> other.getName().equals(sourceSet.getName())));
                    });
                });
            });
        });
    }
}
