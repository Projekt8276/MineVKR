package com.helixd2s.minevkr.ducks

import net.minecraft.util.Identifier
import java.util.*

interface IETexture {
    open fun id(): Optional<Identifier>;
}
