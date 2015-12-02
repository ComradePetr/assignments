package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Injector {
    private static Class<?>[] implementationClasses = null;
    private static HashMap<Class<?>, Object> saved = new HashMap<Class<?>, Object>();

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        implementationClasses = new Class<?>[implementationClassNames.size() + 1];
        implementationClasses[0] = Class.forName(rootClassName);
        saved.clear();
        int i = 1;
        for (String implementationClassName : implementationClassNames) {
            implementationClasses[i++] = Class.forName(implementationClassName);
        }

        Class<?> rootClass = Class.forName(rootClassName);
        return initialize(rootClass);
    }

    private static Object initialize(Class<?> rootClass) throws Exception {
        if (saved.containsKey(rootClass)) {
            Object createdObject = saved.get(rootClass);
            if (createdObject == null) {
                throw new InjectionCycleException();
            } else {
                return createdObject;
            }
        }
        saved.put(rootClass, null);

        Constructor<?>[] constructors = rootClass.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException();
        }

        Constructor<?> constructor = constructors[0];
        ArrayList<Object> parametersList = new ArrayList<>();
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            ArrayList<Class<?>> candidates = new ArrayList<>();
            for (Class<?> curClass : implementationClasses) {
                if (parameterType.isAssignableFrom(curClass)) {
                    candidates.add(curClass);
                }
            }
            if (candidates.isEmpty()) {
                throw new ImplementationNotFoundException();
            }
            if (candidates.size() > 1) {
                throw new AmbiguousImplementationException();
            }
            parametersList.add(initialize(candidates.get(0)));
        }

        Object createdObject = constructor.newInstance(parametersList.toArray());
        saved.put(rootClass, createdObject);
        return createdObject;
    }
}