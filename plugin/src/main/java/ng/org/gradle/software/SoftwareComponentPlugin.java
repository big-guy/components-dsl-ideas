package ng.org.gradle.software;

import ng.org.gradle.jvm.model.JvmFeature;
import ng.org.gradle.jvm.model.JvmTarget;
import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.LibraryComponent;
import ng.org.gradle.software.model.Model;
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.List;

public abstract class SoftwareComponentPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Model model = project.getExtensions().create(Model.class, "ng", Model.class);
        ExtensiblePolymorphicDomainObjectContainer<Component> components = model.getNgComponents();
        // TODO: Should be register as part of SoftwareLibrary or a base SoftwareLibrary plugin?
        components.registerBinding(LibraryComponent.class, LibraryComponent.class);

        project.getTasks().register("printModel", t -> {
            t.doLast(task -> {
                IndentedPrintStream out = new IndentedPrintStream(System.out);
                model.getNgComponents().forEach(component -> {
                    out.println("Component: " + component.getName() + " {");
                    out.indent();
                    component.getFeatures().forEach(feature -> {
                        out.println("Feature: " + feature.getName() + " {");
                        out.indent();
                        // TODO: implement a proper renderer so this can be kept generic
                        if (feature instanceof JvmFeature) {
                            out.println("JDKs: " + ((JvmFeature) feature).getTargetJdks().map(jdks -> jdks.toString()).getOrElse("(unset)"));
                            ((JvmFeature) feature).getSources().forEach(source -> {
                                out.println("Common source: " + source.getName());
                                out.indent();
                                source.getSrcDirs().getFiles().forEach(srcDir -> {
                                    out.println(srcDir);
                                });
                                out.dedent();
                            });

                            out.println("Dependencies: ");
                            out.indent();
                            List<String> apiDependencies = ((JvmFeature) feature).getDependencies().getApiDependencies().get();
                            if (!apiDependencies.isEmpty()) {
                                apiDependencies.forEach(dep -> {
                                    out.println("api " + dep);
                                });
                            } else {
                                out.println("No api dependencies.");
                            }
                            List<String> implementationDependencies = ((JvmFeature) feature).getDependencies().getImplementationDependencies().get();
                            if (!implementationDependencies.isEmpty()) {
                                implementationDependencies.forEach(dep -> {
                                    out.println("implementation " + dep);
                                });
                            } else {
                                out.println("No implementation dependencies.");
                            }
                            out.dedent();
                        }
                        feature.getTargets().forEach(target -> {
                            out.println("Target: " + target.getName() + " {");
                            out.indent();
                            if (target instanceof JvmTarget) {
                                out.println("JDK: " + ((JvmTarget) target).getTargetJdk().map(jdks -> jdks.toString()).getOrElse("(unset)"));

                                ((JvmTarget) target).getSources().forEach(source -> {
                                    out.println("Source: " + source.getName());
                                    out.indent();
                                    source.getSrcDirs().getFiles().forEach(srcDir -> {
                                        out.println(srcDir);
                                    });
                                    out.dedent();
                                });

                                out.println("Dependencies: ");
                                out.indent();
                                List<String> apiDependencies = ((JvmTarget) target).getDependencies().getApiDependencies().get();
                                if (!apiDependencies.isEmpty()) {
                                    apiDependencies.forEach(dep -> {
                                        out.println("api " + dep);
                                    });
                                } else {
                                    out.println("No api dependencies.");
                                }
                                List<String> implementationDependencies = ((JvmTarget) target).getDependencies().getImplementationDependencies().get();
                                if (!implementationDependencies.isEmpty()) {
                                    implementationDependencies.forEach(dep -> {
                                        out.println("implementation " + dep);
                                    });
                                } else {
                                    out.println("No implementation dependencies.");
                                }
                                out.dedent();

                            }
                            out.dedent();
                            out.println("}");
                        });
                        out.dedent();
                        out.indent();
                        feature.getVariants().forEach(variant -> {
                            out.println("Variant: " + variant.getName() + " {");
                            out.indent();
                            out.println(variant);
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
