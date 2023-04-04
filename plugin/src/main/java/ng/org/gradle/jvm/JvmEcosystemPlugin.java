package ng.org.gradle.jvm;

import ng.org.gradle.jvm.model.JvmFeature;
import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.Model;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

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
                // set conventions for source sets in a JVM feature
                feature.getSources().configureEach(sourceSet -> {
                    sourceSet.srcDir("src/" + feature.getName() + "/" + sourceSet.getName());
                });
                feature.getTargets().configureEach(target -> {
                    target.getDependencies().getImplementationDependencies().addAll(feature.getDependencies().getImplementationDependencies());
                    target.getDependencies().getApiDependencies().addAll(feature.getDependencies().getApiDependencies());
                    target.getSources().configureEach(sourceSet -> {
                        // TODO: how do we figure out the target name?
                        sourceSet.srcDir("src/" + target.getName() + "/" + sourceSet.getName());
                        // TODO: better way to express "include common source set with the same name"?
                        sourceSet.getSrcDirs().from(feature.getSources().matching(other -> other.getName().equals(sourceSet.getName())));
                    });
                });
            });
        });
    }
}
