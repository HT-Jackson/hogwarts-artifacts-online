package edu.tcu.cs.hogwartsartifactsonline.wizard;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;
    @InjectMocks
    WizardService wizardService;
    List<Wizard> wizards;
    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albeus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
        this.wizards.add(w3);
    }
    @AfterEach
    void tearDown() {
    }
    @Test
    void testFindAllSuccess() {
        //given, arrange inputs and targets, define the behavior of mock objects
        given(this.wizardRepository.findAll()).willReturn(this.wizards);

        //when, act on the target behavior, act steps should cover the method to be tested
        List<Wizard> actualWizards = this.wizardService.findAll();

        //then, assert expected outcomes
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());

        //then, verify the behavior of mock objects
        verify(this.wizardRepository, times(1)).findAll();)
    }

    @Test
    void testFindByIdSuccess() {
        //given , arrange inputs and targets, define the behavior of mock objects
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(w));

        //when, act on the target behavior, act steps should cover the method to be tested
        Wizard returnedWizard = this.wizardService.findById(1);

        //then, assert expected outcomes
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());

        //then, verify the behavior of mock objects
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        //given
        given(this.wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(() -> {
           Wizard returnedWizard = this.wizardService.findById(1);
        });

        //then
        assertThat(thrown)
                .isInstanceOf(WizardNotFoundException.class)
                        .hasMessage("Could not find wizard with Id 1");
        verify(this.wizardRepository, times(1)).findById(Mockito.any(Integer.class));

        //then, verify the behavior of mock objects
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSuccess() {
        //given
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");

        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);

        //when
        Wizard returnedWizard = this.wizardService.save(newWizard);

        //then
        assertThat(returnedWizard.getName()).isEqualTo(newWizard.getName());

        //then, verify the behavior of mock objects
        verify(this.wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess() {
        //given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");

        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - update");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //when
        Wizard updatedWizard = this.wizardService.update(1, update);

        //then
        assertThat(updatedWizard.getId()).isEqualTo(1);
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());

        //then, verify the behavior of mock objects
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound() {
        //given
        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - update");

        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        //when
        assertThrows(WizardNotFoundException.class, () -> {
           this.wizardService.update(1, update);
        });

        //then

        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        //given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(this.wizardRepository).deleteById(1);

        //when
        this.wizardService.delete(1);

        //then, verify the behavior of mock objects
        verify(this.wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        //given
        given(this.wizardRepository.findById(1).willReturn(Optional.empty());

        //when
        assertThrows(WizardNotFoundException.class, () -> {
           this.wizardService.delete(1);
        });

        //then, verify the behavior of mock objects
        verify(this.wizardRepository, times(1)).findById(1);
    }


}