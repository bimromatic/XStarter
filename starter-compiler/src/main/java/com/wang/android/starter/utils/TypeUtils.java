package com.wang.android.starter.utils;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class TypeUtils {

    private Types types;
    private TypeMirror parcelableType;
    private TypeMirror serializableType;

    public TypeUtils(Types types, Elements elements) {
        this.types = types;

        parcelableType = elements.getTypeElement(Constants.PARCELABLE).asType();
        serializableType = elements.getTypeElement(Constants.SERIALIZABLE).asType();
    }

    /**
     * Diagnostics out the true java type
     *
     * @param element Raw type
     * @return Type class of java
     */
    public int typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();

        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case Constants.BYTE:
                return TypeKind.BYTE.ordinal();
            case Constants.SHORT:
                return TypeKind.SHORT.ordinal();
            case Constants.INTEGER:
                return TypeKind.INT.ordinal();
            case Constants.LONG:
                return TypeKind.LONG.ordinal();
            case Constants.FLOAT:
                return TypeKind.FLOAT.ordinal();
            case Constants.DOUBEL:
                return TypeKind.DOUBLE.ordinal();
            case Constants.BOOLEAN:
                return TypeKind.BOOLEAN.ordinal();
            case Constants.CHAR:
                return TypeKind.CHAR.ordinal();
            case Constants.STRING:
                return TypeKind.STRING.ordinal();
            default:
                // Other side, maybe the PARCELABLE or SERIALIZABLE or OBJECT.
                if (types.isSubtype(typeMirror, parcelableType)) {
                    // PARCELABLE
                    return TypeKind.PARCELABLE.ordinal();
                } else if (types.isSubtype(typeMirror, serializableType)) {
                    // SERIALIZABLE
                    return TypeKind.SERIALIZABLE.ordinal();
                } else {
                    return TypeKind.OBJECT.ordinal();
                }
        }
    }
    
}
