package org.rsultan.app.convert;

public interface Converter<T, U> {

  T convert(U source);

}
