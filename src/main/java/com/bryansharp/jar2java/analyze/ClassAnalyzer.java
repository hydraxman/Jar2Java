package com.bryansharp.jar2java.analyze;

import com.bryansharp.jar2java.analyze.entity.VisitedClass;

import java.util.List;
import java.util.Map;

/**
 * Created by bushaopeng on 18/1/25.
 */
public interface ClassAnalyzer {

    byte[] modifyClass(String classname, byte[] classBytes, Map<String, VisitedClass> visitedClassMap);
}
