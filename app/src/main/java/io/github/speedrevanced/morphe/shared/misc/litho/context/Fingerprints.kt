package io.github.speedrevanced.morphe.shared.misc.litho.context

import io.github.speedrevanced.morphe.findClassDirect
import io.github.speedrevanced.morphe.findFieldDirect
import io.github.speedrevanced.morphe.youtube.shared.conversionContextFingerprintToString
import org.luckypray.dexkit.result.FieldUsingType

val conversionContextClass = findClassDirect {
    conversionContextFingerprintToString(this).declaredClass!!
}
val identifierFieldData = findFieldDirect {
    val stringFieldIndex =
        if (findMethod { matcher { usingStrings(", pathInternal=") } }.any()) 2 else 1
    conversionContextClass(this).methods.single {
        it.isConstructor && it.paramCount != 0
    }.usingFields.filter {
        it.usingType == FieldUsingType.Write && it.field.typeName == String::class.java.name
    }[stringFieldIndex].field
}

val pathBuilderFieldData = findFieldDirect {
    conversionContextClass(this).fields.single { it.typeSign == "Ljava/lang/StringBuilder;" }
}
