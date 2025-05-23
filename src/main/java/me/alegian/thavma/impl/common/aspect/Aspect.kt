package me.alegian.thavma.impl.common.aspect

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import me.alegian.thavma.impl.init.registries.T7Registries.ASPECT
import net.minecraft.Util
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class Aspect(var id: String, var color: Int, var components: List<Supplier<Aspect>>) {
  val isPrimal: Boolean
    get() = components.isEmpty()
  val translationId by lazy {
    Util.makeDescriptionId(ASPECT.key().location().path, ASPECT.getKey(this))
  }
  val rank: Int by lazy {
    if (isPrimal) 1
    else components.maxOf { a -> a.get().rank } + 1
  }

  companion object {
    val STREAM_CODEC: StreamCodec<ByteBuf, Aspect> = ByteBufCodecs.STRING_UTF8.map(
      { s: String -> ASPECT[ResourceLocation.parse(s)] },
      { a: Aspect -> ASPECT.getKey(a).toString() }
    )
    val CODEC: Codec<Aspect> = ASPECT.byNameCodec()
  }

  fun defaultStack() = AspectStack.of(this, 1)
}
