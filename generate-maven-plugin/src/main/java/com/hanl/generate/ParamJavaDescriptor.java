package com.hanl.generate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.Iterator;
import java.util.List;

/**
 * @author: Hanl
 * @date :2020/3/11
 * @desc:
 */
public class ParamJavaDescriptor {

    private String packageDir;

    private String className;

    private List<FieldDescription> fieldList;

    public ParamJavaDescriptor(String packageDir, String className, List<FieldDescription> fieldList) {
        this.packageDir = packageDir;
        this.className = className;
        this.fieldList = fieldList;
    }

    public CompilationUnit createCompilationUnit() {
        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setPackageDeclaration(packageDir);
        Iterator<FieldDescription> it = fieldList.iterator();
        ClassOrInterfaceDeclaration classDeclaration = compilationUnit.addClass(className).setPublic(true);
        while (it.hasNext()) {
            FieldDescription fieldDeclaration = it.next();
            String fieldName = fieldDeclaration.getName();
            String fieldType = fieldDeclaration.getFieldType();
            compilationUnit.addImport(fieldType);
            classDeclaration.addPublicField(fieldType, fieldName);
        }
        return compilationUnit;
    }
}
