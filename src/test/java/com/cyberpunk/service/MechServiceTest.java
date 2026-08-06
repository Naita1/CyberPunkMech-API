package com.cyberpunk.service;

import com.cyberpunk.dto.AttackMechRequest;
import com.cyberpunk.dto.AttackMechResponse;
import com.cyberpunk.dto.DefensiveMechRequest;
import com.cyberpunk.dto.DefensiveMechResponse;
import com.cyberpunk.model.AttackMech;
import com.cyberpunk.model.DefensiveMech;
import com.cyberpunk.model.Mech;
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
class MechServiceTest {

    @Mock private Firestore firestore;
    @Mock private CollectionReference collectionReference;
    @Mock private DocumentReference documentReference;
    @Mock private ApiFuture<WriteResult> writeResultFuture;
    @Mock private ApiFuture<DocumentSnapshot> snapshotFuture;
    @Mock private DocumentSnapshot documentSnapshot;

    @InjectMocks
    private MechService mechService;

    @BeforeEach
    void setUp() {
        when(firestore.collection(anyString())).thenReturn(collectionReference);
    }

    @Test
    void saveAttackMech_shouldReturnCorrectResponse() throws Exception {
        when(collectionReference.document(anyString())).thenReturn(documentReference);
        doReturn(writeResultFuture).when(documentReference).set(any(AttackMech.class));
        when(writeResultFuture.get()).thenReturn(mock(WriteResult.class));

        AttackMechRequest request = new AttackMechRequest("mech-01", "player-01", "Viper-X", 100, 80, 20, 100);
        AttackMechResponse response = mechService.saveAttackMech(request);

        assertEquals("mech-01", response.idMech());
        assertEquals("player-01", response.playerId());
        assertEquals("ATTACK", response.type());
        assertEquals(100, response.currentHealth());
        assertEquals(0, response.heatLevel());
    }

    @Test
    void saveDefensiveMech_shouldReturnCorrectResponse() throws Exception {
        when(collectionReference.document(anyString())).thenReturn(documentReference);
        doReturn(writeResultFuture).when(documentReference).set(any(DefensiveMech.class));
        when(writeResultFuture.get()).thenReturn(mock(WriteResult.class));

        DefensiveMechRequest request = new DefensiveMechRequest("mech-02", "player-01", "Aegis", 150, 70, 50);
        DefensiveMechResponse response = mechService.saveDefensiveMech(request);

        assertEquals("mech-02", response.idMech());
        assertEquals("DEFENSIVE", response.type());
        assertEquals(50, response.shieldArmor());
        assertFalse(response.shieldActive());
    }

    @Test
    void getMechById_whenNotFound_shouldReturnNull() throws Exception {
        when(collectionReference.document(anyString())).thenReturn(documentReference);
        when(documentReference.get()).thenReturn(snapshotFuture);
        when(snapshotFuture.get()).thenReturn(documentSnapshot);
        when(documentSnapshot.exists()).thenReturn(false);

        Mech result = mechService.getMechById("mech-inexistente");

        assertNull(result);
    }

    @Test
    void getMechById_whenFound_shouldReturnAttackMech() throws Exception {
        AttackMech attackMech = new AttackMech("mech-01", "player-01", "Viper-X", 100, 80, 20, 100);

        when(collectionReference.document(anyString())).thenReturn(documentReference);
        when(documentReference.get()).thenReturn(snapshotFuture);
        when(snapshotFuture.get()).thenReturn(documentSnapshot);
        when(documentSnapshot.exists()).thenReturn(true);
        when(documentSnapshot.getString("type")).thenReturn("ATTACK");
        when(documentSnapshot.toObject(AttackMech.class)).thenReturn(attackMech);

        Mech result = mechService.getMechById("mech-01");

        assertNotNull(result);
        assertEquals("ATTACK", result.getType());
    }

    @Test
    void getMechsByPlayerId_shouldReturnEmptyList() throws Exception {
        ApiFuture<QuerySnapshot> queryFuture = mock(ApiFuture.class);
        QuerySnapshot querySnapshot = mock(QuerySnapshot.class);
        Query query = mock(Query.class);

        when(collectionReference.whereEqualTo(anyString(), anyString())).thenReturn(query);
        when(query.get()).thenReturn(queryFuture);
        when(queryFuture.get()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(List.of());

        List<Mech> result = mechService.getMechsByPlayerId("player-sem-mechs");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getMechById_withUnknownType_shouldThrowIllegalArgumentException() throws Exception {
        when(collectionReference.document(anyString())).thenReturn(documentReference);
        when(documentReference.get()).thenReturn(snapshotFuture);
        when(snapshotFuture.get()).thenReturn(documentSnapshot);
        when(documentSnapshot.exists()).thenReturn(true);
        when(documentSnapshot.getString("type")).thenReturn("UNKNOWN");
        when(documentSnapshot.getId()).thenReturn("mech-invalido");

        assertThrows(IllegalArgumentException.class, () -> mechService.getMechById("mech-invalido"));
    }
}
