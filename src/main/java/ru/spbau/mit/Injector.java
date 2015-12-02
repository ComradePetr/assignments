package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Injector {

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    static Class<?>[] implementationClasses = null;
    static HashMap<Class<?>, Object> saved = new HashMap<Class<?>,Object>();

    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        implementationClasses = new Class<?>[implementationClassNames.size()];
        saved.clear();
        int i = 0;
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
            throw new IllegalStateException();
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

        return constructor.newInstance(parametersList.toArray());
    }
}