/*
 * Copyright 2016 yihtserns.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.yihtserns.groovy.decorator;

import groovy.lang.Closure;
import java.util.List;

/**
 * Since I can't set any property to a {@code Closure}, I use this wrapper class to do it.
 *
 * @author yihtserns
 */
public class Function extends Closure {

    protected Closure delegate;
    private String methodName;
    private Class<?> returnType;

    private Function(Closure delegate, String methodName, Class<?> returnType) {
        super(null);
        this.delegate = delegate;
        this.methodName = methodName;
        this.returnType = returnType;
        setResolveStrategy(TO_SELF);
    }

    public Object doCall(List<Object> args) {
        return delegate.call(args.toArray(new Object[args.size()]));
    }

    /**
     * @return method name
     */
    public String getName() {
        return methodName;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public Closure decorateWith(Closure<Closure> decorator) {
        return decorateWith(null, decorator);
    }

    public Closure decorateWith(Object baggage, Closure<Closure> decorator) {
        Closure decorated = decorator.getMaximumNumberOfParameters() == 1
                ? decorator.call(this)
                : decorator.call(this, baggage);

        return new DecoratedFunction(decorated, methodName, returnType);
    }

    public static Function create(Closure delegate, String methodName, Class<?> returnType) {
        return new Function(delegate, methodName, returnType);
    }

    private static final class DecoratedFunction extends Function {

        public DecoratedFunction(Closure delegate, String methodName, Class<?> returnType) {
            super(delegate, methodName, returnType);
        }

        @Override
        public Object doCall(List<Object> args) {
            return delegate.call(args);
        }
    }
}
