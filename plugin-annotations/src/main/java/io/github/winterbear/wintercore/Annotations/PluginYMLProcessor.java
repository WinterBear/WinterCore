package io.github.winterbear.wintercore.Annotations;

import com.google.auto.service.AutoService;
import org.bukkit.Server;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Created by WinterBear on 10/06/2019.
 */
@SupportedAnnotationTypes({
        "io.github.winterbear.wintercore.Annotations.Command", "io.github.winterbear.wintercore.Annotations.SpigotPlugin"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class PluginYMLProcessor extends AbstractProcessor {

    private FileObject pluginYML;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            if(pluginYML == null){
                pluginYML = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "","plugin.yml");
            }

            Set<? extends Element> commands = Collections.emptySet();
            String pluginName = null;
            String pluginVersion = "";
            String pluginMain = "";
            for(TypeElement annotation : annotations) {
                if (annotation.getSimpleName().toString().contains("Command")) {
                    commands = roundEnv.getElementsAnnotatedWith(annotation);
                    System.out.println("Commands: " + commands.toString());
                } else if (annotation.getSimpleName().toString().contains("SpigotPlugin")) {
                    Optional<? extends Element> pluginRoot = roundEnv.getElementsAnnotatedWith(annotation).stream().findFirst();
                    if (pluginRoot.isPresent()) {
                        pluginName = pluginRoot.get().getSimpleName().toString();
                        pluginVersion = pluginRoot.get().getAnnotation(SpigotPlugin.class).version();
                        pluginMain = pluginRoot.get().asType().toString();
                        System.out.println("Plugin Name Detected: " + pluginName);
                        System.out.println("Plugin Version Detected: " + pluginVersion);
                        System.out.println("Plugin Main Detected: " + pluginMain);

                    } else {
                        System.out.println("ERROR: Plugin Root Element could not be found");
                    }


                } else {
                    System.out.println("ERROR: UNKNOWN ANNOTATION - " + annotation.getQualifiedName());
                }

            }
            if(pluginName != null) {
                try (PrintWriter out = new PrintWriter(pluginYML.openWriter())) {
                    out.println("name: " + pluginName);
                    out.println("main: " + pluginMain);
                    out.println("version: " + pluginVersion);
                    out.println("api-version: 1.13");
                    out.println("commands: ");
                    commands.stream().forEach(c -> printCommand(c, out));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    public void printCommand(Element command, PrintWriter out){
        String commandParsed = command.toString().replace("()", "");
        out.println("  " + commandParsed + ":");
        String aliases = command.getAnnotation(Command.class).aliases();
        String description = command.getAnnotation(Command.class).description();
        String usage = command.getAnnotation(Command.class).usage();
        String permission = command.getAnnotation(Command.class).permission();
        if(!aliases.equals("")){
            out.println("    aliases: " + aliases);
        }
        if(!description.equals("")){
            out.println("    description: " + description);
        }
        if(!usage.equals("")){
            out.println("    usage: " + usage);
        }
        if(!permission.equals("")){
            out.println("    permission: " + permission);
        }

    }



}
