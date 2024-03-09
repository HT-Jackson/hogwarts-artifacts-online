package edu.tcu.cs.hogwartsartifactsonline.wizard.dto;

public record WizardDto(Integer id,
                       // @NotEmpty(message = "name is required.")
                        String name,
                        Integer numberOfArtifacts) {
}
