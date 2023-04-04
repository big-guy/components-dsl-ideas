package ng.org.gradle.software;

import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.Model;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public abstract class SoftwareComponentPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Model model = project.getExtensions().create(Model.class, "ng", Model.class);

        project.getTasks().register("printModel", t -> {
            t.doLast(task -> {
                IndentedPrintStream out = new IndentedPrintStream(System.out);
                model.getComponents().forEach(component -> {
                    out.println("Component: " + component.getName() + " {");
                    out.indent();
                    component.getFeatures().forEach(feature -> {
                        out.println("Feature: " + feature.getName() + " {");
                        out.indent();
                        feature.getTargets().forEach(target -> {
                            out.println("Target: " + target.getName() + " {");
                            out.indent();
                            out.dedent();
                            out.println("}");
                        });
                        out.dedent();
                        out.indent();
                        feature.getVariants().forEach(variant -> {
                            out.println("Variant: " + variant.getName() + " {");
                            out.indent();
                            out.dedent();
                            out.println("}");
                        });
                        out.dedent();
                        out.println("}");
                    });
                    out.dedent();
                    out.println("}");
                });
            });
        });
    }
}
