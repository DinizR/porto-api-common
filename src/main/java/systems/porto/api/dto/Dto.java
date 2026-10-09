package systems.porto.api.dto;

import systems.porto.dto.DTO;

/**
 * Plugin DTO with a typed identity. Extends porto-core {@link DTO} the same way
 * {@link systems.porto.model.Model} types a domain id.
 */
public interface Dto<T> extends DTO {

    T id();
}
