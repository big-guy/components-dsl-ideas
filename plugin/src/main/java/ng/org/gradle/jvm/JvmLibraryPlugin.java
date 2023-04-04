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
        model.getComponents().withType(LibraryComponent.class).named(project.getName(), component -> {
            component.getFeatures().registerBinding(JvmLibraryFeature.class, JvmLibraryFeature.class);
            component.getFeatures().register("main", JvmLibraryFeature.class);
        });
        model.getComponents().withType(Component.class).configureEach(component -> {
            component.getFeatures().withType(JvmLibraryFeature.class).configureEach(feature -> {
                feature.getTargets().registerBinding(JvmLibraryTarget.class, JvmLibraryTarget.class);
                feature.getTargets().addAllLater(feature.getTargetJdks().map(jdks -> jdks.stream().map(jdk -> feature.getTargets().create(feature.getName() + jdk, JvmLibraryTarget.class)).collect(Collectors.toList())));
            });
        });
    }
}
