package pt.ul.fc.css.soccernow.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.UtilizadorDTO;

public class DataModel {
    
    // currently logged in user
    private final ObjectProperty<UtilizadorDTO> authenticatedUtilizador = new SimpleObjectProperty<>(null);

    public ObjectProperty<UtilizadorDTO> authenticatedUtilizadorProperty() {
		return authenticatedUtilizador;
	}

	public final UtilizadorDTO getAuthenticatedUtilizador() {
		return authenticatedUtilizadorProperty().get();
	}

	public final void setAuthenticatedUtilizador(UtilizadorDTO utilizador) {
		authenticatedUtilizadorProperty().set(utilizador);
	}

	private final ObjectProperty<CampeonatoDTO> selectedCampeonato = new SimpleObjectProperty<>(null);

	public ObjectProperty<CampeonatoDTO> selectedCampeonatoProperty() {
		return selectedCampeonato;
	}

	public final CampeonatoDTO getSelectedCampeonato() {
		return selectedCampeonatoProperty().get();
	}

	public final void setSelectedCampeonato(CampeonatoDTO campeonato) {
		selectedCampeonatoProperty().set(campeonato);
	}

	private final ObjectProperty<ClubeDTO> selectedClube = new SimpleObjectProperty<>(null);

	public ObjectProperty<ClubeDTO> selectedClubeProperty() {
		return selectedClube;
	}

	public final ClubeDTO getSelectedClube() {
		return selectedClubeProperty().get();
	}

	public final void setSelectedClube(ClubeDTO clube) {
		selectedClubeProperty().set(clube);
	}

	private final ObjectProperty<EquipaDTO> selectedEquipa = new SimpleObjectProperty<>(null);

	public ObjectProperty<EquipaDTO> selectedEquipaProperty() {
		return selectedEquipa;
	}

	public final EquipaDTO getSelectedEquipa() {
		return selectedEquipaProperty().get();
	}

	public final void setSelectedEquipa(EquipaDTO equipa) {
		selectedEquipaProperty().set(equipa);
	}

	private final ObjectProperty<UtilizadorDTO> selectedUtilizador = new SimpleObjectProperty<>(null);

	public ObjectProperty<UtilizadorDTO> selectedUtilizadorProperty() {
		return selectedUtilizador;
	}

	public final UtilizadorDTO getSelectedUtilizador() {
		return selectedUtilizadorProperty().get();
	}

	public final void setSelectedUtilizador(UtilizadorDTO utilizador) {
		selectedUtilizadorProperty().set(utilizador);
	}
}
