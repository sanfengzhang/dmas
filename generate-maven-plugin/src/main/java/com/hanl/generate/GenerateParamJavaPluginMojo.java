package com.hanl.generate;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: Hanl
 * @date :2020/3/10
 * @desc:
 */
@Mojo(name = "generateParamJava", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class GenerateParamJavaPluginMojo extends AbstractMojo {

    //项目根目录 如：E:\Research\maven_plugin
    @Parameter(name = "basedir", defaultValue = "${project.basedir}", required = true, readonly = true)
    private File basedir;

    @Parameter(name = "generateDir", required = true, readonly = true)
    private String generateDir;

    @Parameter(name = "packagePath", required = true, readonly = true)
    private String packagePath;


    private String getJavaSrcPath() {

        return String.format("%s/src/main/java", basedir.getAbsolutePath());
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<File> files = getNeedGenerateFile();
        for (File file : files) {
            try {
                ParseResult<CompilationUnit> parseResult = new JavaParser().parse(file);
                CompilationUnit compilationUnit = parseResult.getResult().get();
                Optional<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationOptional = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class);
                if (!classOrInterfaceDeclarationOptional.isPresent()) {
                    continue;
                }
                ClassOrInterfaceDeclaration classOrInterfaceDeclaration = classOrInterfaceDeclarationOptional.get();
                Optional<AnnotationExpr> optionalAnnotationExpr = classOrInterfaceDeclaration.getAnnotationByName("OperatorDescription");
                if (!optionalAnnotationExpr.isPresent()) {
                    continue;
                }
                AnnotationExpr classAnnotation = optionalAnnotationExpr.get();
                String className = null;
                List<Node> classNodes = classAnnotation.getChildNodes();
                for (Node clasNode : classNodes) {
                    String nodeString = clasNode.toString();
                    if (nodeString.contains("opName")) {
                        className = nodeString.split("=")[1].replace("\"", "").trim();
                        String firstChar = className.substring(0, 1).toUpperCase();
                        className = firstChar + className.substring(1, className.length());
                        break;
                    }
                }
                if (null == className) {
                    continue;
                }

                List<FieldDeclaration> fieldDeclarationList = classOrInterfaceDeclaration.getFields();
                List<FieldDescription> result = new ArrayList<>();
                for (FieldDeclaration fieldDeclaration : fieldDeclarationList) {
                    if (!fieldDeclaration.getAnnotationByName("OperatorParamDescription").isPresent()) {
                        continue;
                    }
                    AnnotationExpr consumer = fieldDeclaration.getAnnotationByName("OperatorParamDescription").get();
                    List<Node> nodeList = consumer.getChildNodes();
                    FieldDescription filedDescription = new FieldDescription();
                    for (Node node : nodeList) {
                        String nodeString = node.toString();
                        if (nodeString.contains("paramName")) {
                            String nodeArray[] = nodeString.split("=");
                            filedDescription.name = nodeArray[1].replace("\"", "");
                        }
                        if (nodeString.contains("paramType")) {
                            String nodeArray[] = nodeString.split("=");
                            filedDescription.fieldType = nodeArray[1].replace("\"", "");
                        }
                    }
                    result.add(filedDescription);
                }
                ParamJavaDescriptor paramJavaDescriptor = new ParamJavaDescriptor(packagePath, className+"VO", result);
                String code = paramJavaDescriptor.createCompilationUnit().toString();
                FileOutputStream fileOutputStream = new FileOutputStream(new File(generateDir + "/" + className + "VO.java"));
                fileOutputStream.write(code.getBytes("UTF-8"));
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<File> getNeedGenerateFile() {
        File file = new File(getJavaSrcPath());
        List<File> result = new ArrayList<>();
        listFile(file, result);
        return result;
    }

    private void listFile(File file, List<File> result) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                listFile(f, result);
            }
        } else {
            if (file.getName().contains(".java")) {
                result.add(file);
            }
        }

    }
}
