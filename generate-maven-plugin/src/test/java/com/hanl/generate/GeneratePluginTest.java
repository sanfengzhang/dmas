package com.hanl.generate;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author: Hanl
 * @date :2020/3/11
 * @desc:
 */
public class GeneratePluginTest {

    @Test
    public void testGenerate() {
        try {
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(new File("D:\\eclipse-workspace\\flusso\\dams\\Etl-foundation\\src\\main\\java\\com\\hanl\\etl\\operator\\Split.java"));
            CompilationUnit compilationUnit = parseResult.getResult().get();
            Optional<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationOptional = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class);

            List<FieldDeclaration> fieldDeclarationList = classOrInterfaceDeclarationOptional.get().getFields();
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

            System.out.println(result);
            ParamJavaDescriptor paramJavaDescriptor = new ParamJavaDescriptor("com.hanl.generate", "SplitVO", result);
            System.out.println(paramJavaDescriptor.createCompilationUnit().toString());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
