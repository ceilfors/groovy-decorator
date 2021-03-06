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
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to describe how to decorate a method:
 * <pre>
 *{@code @}MethodDecorator({ &lt;how to decorate method&gt; })
 *{@code @}GroovyASTTransformationClass("com.github.yihtserns.groovy.decorator.DecoratorASTTransformation")
 *{@code @}Retention(RetentionPolicy.RUNTIME)
 *{@code @}Target(ElementType.METHOD)
 *{@code @} interface AnAnnotation {
 * }
 * </pre>
 * which is then used to annotate a method to decorate it:
 * <pre>
 *{@code @}AnAnnotation
 * &lt;the method&gt;(...)
 * </pre>
 * @see #value()
 * @author yihtserns
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MethodDecorator {

   /**
    * Returns a closure in this form:
    * <pre>
    * { com.github.yihtserns.groovy.decorator.Function func -&gt; // Representing the decorated method
    *
    *   return { args -&gt; // A list of arguments the caller used to call the method
    *
    *     // return func(args) // Call the decorated method and return the result
    *     // Or do whatever
    *   }
    * }
    * </pre>
    * To reference the decorator annotation, add a second parameter to the closure:
    * <pre>
    *  // Representing the decorated method, and the annotation annotated on the method
    * { com.github.yihtserns.groovy.decorator.Function func, AnAnnotation theAnnotation -&gt;
    *
    *   // Do whatever you want with theAnnotation
    *
    *   return { args -&gt; // A list of arguments the caller used to call the method
    *
    *     // return func(args) // Call the decorated method and return the result
    *     // Or do whatever
    *   }
    * }
    * </pre>
    * @return a closure that decorates a method,
    * @see MethodDecorator
    */
    Class<? extends Closure> value();
}
