package io.github.speedrevanced.morphe.youtube.misc.litho.node

import io.github.speedrevanced.morphe.shared.misc.litho.context.conversionContextPatch
import io.github.speedrevanced.morphe.shared.misc.litho.node.createTreeNodeElementHookPatch
import io.github.speedrevanced.patch

val TreeNodeElementHook = createTreeNodeElementHookPatch(
    patch {},
    conversionContextPatch,
    false,
    useLegacyContextRegister = { false }
)