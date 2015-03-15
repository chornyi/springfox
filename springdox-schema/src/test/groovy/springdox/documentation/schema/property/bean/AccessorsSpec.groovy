package springdox.documentation.schema.property.bean

import spock.lang.Specification
import springdox.documentation.schema.TypeForTestingPropertyNames
import springdox.documentation.schema.TypeWithGettersAndSetters

import static springdox.documentation.schema.property.bean.Accessors.*

class AccessorsSpec extends Specification {
  def "Cannot instantiate the Accessors helper"() {
    when:
      new Accessors()
    then:
      thrown(UnsupportedOperationException)
  }

  def "Property names are identified correctly based on (get/set) method names"() {
    given:
      def sut = TypeForTestingPropertyNames
      def method = sut.methods.find { it.name.equals(methodName) }

    expect:
      propertyName(method) == property

    where:
      methodName       || property
      "getProp"        || "prop"
      "getProp1"       || "prop1"
      "getProp_1"      || "prop_1"
      "isProp"         || "prop"
      "isProp1"        || "prop1"
      "isProp_1"       || "prop_1"
      "setProp"        || "prop"
      "setProp1"       || "prop1"
      "setProp_1"      || "prop_1"
      "prop"           || ""
      "getAnotherProp" || "prop"
      "setAnotherProp" || "prop"
      "getPropFallback"|| "propFallback"
      "setPropFallback"|| "propFallback"
  }

  def "Identifies JsonSetter annotation"() {
    given:
      def sut = TypeForTestingPropertyNames

    when:
      def method = sut.methods.find { it.name.equals(methodName) }

    then:
      isSetter(method)

    where:
      methodName << ["setAnotherProp", "anotherProp"]
  }

  def "Identifies JsonGetter annotation"() {
    given:
      def sut = TypeForTestingPropertyNames

    when:
      def method = sut.methods.find { it.name.equals(methodName) }

    then:
      isGetter(method)

    where:
      methodName << ["yetAnotherProp", "getAnotherProp"]
  }

  def "Getters are identified correctly"() {
    given:
      def sut = TypeWithGettersAndSetters
      def method = sut.methods.find { it.name.equals(methodName) }

    expect:
      isGetter(method) == result

    where:
      methodName      || result
      "getIntProp"    || true
      "setIntProp"    || false
      "isBoolProp"    || true
      "setBoolProp"   || false
      "getVoid"       || false
      "isNotGetter"   || false
      "getWithParam"  || false
      "setNotASetter" || false
  }

  def "Setters are identified correctly"() {
    given:
      def sut = TypeWithGettersAndSetters
      def method = sut.methods.find { it.name.equals(methodName) }

    expect:
      isSetter(method) == result

    where:
      methodName      || result
      "getIntProp"    || false
      "setIntProp"    || true
      "isBoolProp"    || false
      "setBoolProp"   || true
      "getVoid"       || false
      "isNotGetter"   || false
      "getWithParam"  || false
      "setNotASetter" || false
  }
}