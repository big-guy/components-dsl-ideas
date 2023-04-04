package ng.org.gradle.software;

import ng.org.gradle.software.model.LibraryComponent;
import ng.org.gradle.software.model.Model;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * software-library registers LibraryComponent
 */
public abstract class SoftwareLibraryPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("ng.org.gradle.software-component");
        Model model = project.getExtensions().getByType(Model.class);
        model.getNgComponents().register(project.getName(), LibraryComponent.class);
    }
}
