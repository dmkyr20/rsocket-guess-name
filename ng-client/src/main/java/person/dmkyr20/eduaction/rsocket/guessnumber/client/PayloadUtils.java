package person.dmkyr20.eduaction.rsocket.guessnumber.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.rsocket.Payload;
import io.rsocket.metadata.CompositeMetadataCodec;
import io.rsocket.metadata.RoutingMetadata;
import io.rsocket.metadata.TaggingMetadataCodec;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.util.DefaultPayload;
import person.dmkyr20.education.rsocket.guessnumber.api.Routs;

import java.util.List;

public class PayloadUtils {
    public static Payload createValidationPayload(byte[] bytes) {
        return createPayload(Routs.VALIDATION, bytes);
    }

    public static Payload createGuessNumberPayload(byte[] bytes) {
        return createPayload(Routs.GUESS_NUMBER, bytes);
    }

    public static Payload createPayload(String rout, byte[] data) {
        CompositeByteBuf metadata = ByteBufAllocator.DEFAULT.compositeBuffer();
        RoutingMetadata routingMetadata = TaggingMetadataCodec.createRoutingMetadata(
                ByteBufAllocator.DEFAULT,
                List.of(rout));
        CompositeMetadataCodec.encodeAndAddMetadata(
                metadata,
                ByteBufAllocator.DEFAULT,
                WellKnownMimeType.MESSAGE_RSOCKET_ROUTING,
                routingMetadata.getContent());
        ByteBuf byteData = ByteBufAllocator.DEFAULT.buffer().writeBytes(data);
        return DefaultPayload.create(byteData, metadata);
    }
}
