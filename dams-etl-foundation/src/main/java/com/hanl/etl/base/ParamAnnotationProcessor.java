package com.hanl.etl.base;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2020/3/10
 * @desc:
 */
@SupportedAnnotationTypes(value = {"com.hanl.etl.api.OperatorDescription"})
public class ParamAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement element : annotations) {
            System.out.println(element);
        }
        return false;
    }
}
