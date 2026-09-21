package io.github.speedrevanced.morphe.youtube.video.quality

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.InstructionLocation.MatchAfterWithin
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.ResourceType
import io.github.speedrevanced.morphe.accessFlags
import io.github.speedrevanced.morphe.fieldAccess
import io.github.speedrevanced.morphe.findFieldDirect
import io.github.speedrevanced.morphe.findFieldFromToString
import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.findMethodListDirect
import io.github.speedrevanced.morphe.fingerprint
import io.github.speedrevanced.morphe.literal
import io.github.speedrevanced.morphe.methodCall
import io.github.speedrevanced.morphe.opcode
import io.github.speedrevanced.morphe.opcodes
import io.github.speedrevanced.morphe.parameters
import io.github.speedrevanced.morphe.resourceLiteral
import io.github.speedrevanced.morphe.resourceMappings
import io.github.speedrevanced.morphe.returns
import io.github.speedrevanced.morphe.string

internal const val FIXED_RESOLUTION_STRING = ", initialPlaybackVideoQualityFixedResolution="

internal object PlaybackStartParametersToStringFingerprint : Fingerprint(
    name = "toString",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string(FIXED_RESOLUTION_STRING)
    )
)

val InitialResolutionField = findFieldDirect {
    PlaybackStartParametersToStringFingerprint().findFieldFromToString(FIXED_RESOLUTION_STRING)
}

val PlaybackStartParametersInit = findMethodDirect {
    Fingerprint(
        classFingerprint = PlaybackStartParametersToStringFingerprint,
        name = "<init>",
        filters = listOf(
            fieldAccess(
                opcode = Opcode.IPUT_OBJECT,
                reference = InitialResolutionField()
            )
        )
    )()
}

val videoQualityItemOnClickParentFingerprint = fingerprint {
    returns("V")
    strings("VIDEO_QUALITIES_MENU_BOTTOM_SHEET_FRAGMENT")
}

/**
 * Resolves to class found in [videoQualityItemOnClickFingerprint].
 */
val videoQualityItemOnClickFingerprint = fingerprint {
    classFingerprint(videoQualityItemOnClickParentFingerprint)
    methodMatcher { name = "onItemClick" }
}

val videoQualityBottomSheetListFragmentTitle
    get() = resourceMappings[
        "layout",
        "video_quality_bottom_sheet_list_fragment_title",
    ]

val videoQualityMenuViewInflateFingerprint = findMethodListDirect {
    // two matches in versions 20.43.32
    // one match in versions <=v20.42.xx
    findMethod {
        matcher {
            accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
            returns("L")
            parameters("L", "L", "L")
            opcodes(
                Opcode.INVOKE_SUPER,
                Opcode.CONST,
                Opcode.CONST_4,
                Opcode.INVOKE_VIRTUAL,
                Opcode.MOVE_RESULT_OBJECT,
                Opcode.CONST,
                Opcode.INVOKE_VIRTUAL,
                Opcode.MOVE_RESULT_OBJECT,
                Opcode.CONST_16,
                Opcode.INVOKE_VIRTUAL,
                Opcode.CONST,
                Opcode.INVOKE_VIRTUAL,
                Opcode.MOVE_RESULT_OBJECT,
                Opcode.CHECK_CAST,
            )
            literal { videoQualityBottomSheetListFragmentTitle }
        }
    }
}

val ShowVideoQualityQuickMenuFingerprint = findMethodListDirect {
    val matcher = Fingerprint(
        accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
        returnType = "V",
        strings = listOf("VIDEO_QUALITIES_QUICK_MENU_BOTTOM_SHEET_FRAGMENT"),
        filters = listOf(
            opcode(Opcode.MOVE_RESULT),
            opcode(
                opcode = Opcode.IF_NEZ,
                location = MatchAfterWithin(3)
            ),
            methodCall(
                opcode = Opcode.INVOKE_VIRTUAL,
                name = "getSupportFragmentManager",
                location = MatchAfterWithin(3)
            ),
            methodCall(
                opcode = Opcode.INVOKE_VIRTUAL,
                parameters = listOf("L", "Ljava/lang/String;"),
                returnType = "V",
                location = MatchAfterWithin(5)
            )
        )
    ).buildMethodMatcher()
    findMethod { matcher(matcher) }
}

internal object ShortsQualityMenuFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Z"),
    returnType = "V",
    filters = listOf(
        resourceLiteral(
            type = ResourceType.STRING,
            name = "video_quality_unavailable_announcement"
        )
    )
)

internal object ShortsQualityConstructorFingerprint : Fingerprint(
    classFingerprint = ShortsQualityMenuFingerprint,
    name = "<init>",
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IPUT_OBJECT,
            definingClass = "this"
        )
    )
)