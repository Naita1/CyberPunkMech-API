package com.cyberpunk.service;

import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.model.Player;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock private Firestore firestore;
    @Mock private MechService mechService;
    @Mock private CollectionReference collectionReference;
    @Mock private DocumentReference documentReference;
    @Mock private ApiFuture<WriteResult> writeResultFuture;
    @Mock private ApiFuture<DocumentSnapshot> snapshotFuture;
    @Mock private DocumentSnapshot documentSnapshot;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        when(firestore.collection(anyString())).thenReturn(collectionReference);
        when(collectionReference.document(anyString())).thenReturn(documentReference);
    }

    @Test
    void savePlayer_shouldReturnCorrectResponse() throws Exception {
        doReturn(writeResultFuture).when(documentReference).set(any(Player.class));
        when(writeResultFuture.get()).thenReturn(mock(WriteResult.class));

        PlayerRequest request = new PlayerRequest("player-01", "CyberSamurai", 500);
        PlayerResponse response = playerService.savePlayer(request);

        assertEquals("player-01", response.idPlayer());
        assertEquals("CyberSamurai", response.namePlayer());
        assertEquals(500, response.coins());
    }

    @Test
    void savePlayer_withNullCoins_shouldUseDefaultCoins() throws Exception {
        doReturn(writeResultFuture).when(documentReference).set(any(Player.class));
        when(writeResultFuture.get()).thenReturn(mock(WriteResult.class));

        PlayerRequest request = new PlayerRequest("player-01", "CyberSamurai", null);
        PlayerResponse response = playerService.savePlayer(request);

        assertEquals(50, response.coins());
    }

    @Test
    void getPlayerById_whenNotFound_shouldReturnNull() throws Exception {
        when(documentReference.get()).thenReturn(snapshotFuture);
        when(snapshotFuture.get()).thenReturn(documentSnapshot);
        when(documentSnapshot.exists()).thenReturn(false);

        PlayerResponse result = playerService.getPlayerById("player-inexistente");

        assertNull(result);
    }

    @Test
    void getPlayerById_whenFound_shouldReturnPlayerWithGarage() throws Exception {
        Player player = new Player();
        player.setIdPlayer("player-01");
        player.setNamePlayer("CyberSamurai");

        when(documentReference.get()).thenReturn(snapshotFuture);
        when(snapshotFuture.get()).thenReturn(documentSnapshot);
        when(documentSnapshot.exists()).thenReturn(true);
        when(documentSnapshot.toObject(Player.class)).thenReturn(player);
        when(mechService.getMechsByPlayerId("player-01")).thenReturn(List.of());

        PlayerResponse result = playerService.getPlayerById("player-01");

        assertNotNull(result);
        assertEquals("player-01", result.idPlayer());
        verify(mechService).getMechsByPlayerId("player-01");
    }

    @Test
    void deletePlayer_shouldDeleteAllMechsBeforePlayer() throws Exception {
        when(mechService.getMechsByPlayerId("player-01")).thenReturn(List.of());
        doReturn(writeResultFuture).when(documentReference).delete();
        when(writeResultFuture.get()).thenReturn(mock(WriteResult.class));

        playerService.deletePlayer("player-01");

        verify(mechService).getMechsByPlayerId("player-01");
        verify(documentReference).delete();
    }
}
