package fil.univ.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }


    @Test
    public void testMarkValidMoveContinuesGame(){
        // Test : couvre le cas où un joueur marque une case valide et que le jeu continue.
        board.mark(0, 0);
        assertTrue(board.isInProgressMode());// Vérifie que le jeu est toujours en cours après un coup valide.
        assertNull(board.getWinner()); // Vérifie qu'il n'y a pas de gagnant après un coup valide.
        assertEquals(Player.O, board.getCurrentTurn()); // Vérifie que le tour passe au joueur suivant après un coup valide.

    }

    @Test
    public void testMarkValidMoveWinsGame(){
        // Test : couvre le cas où un joueur marque une case valide et gagne la partie.
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(0, 2); // X gagne avec une ligne horizontale sur la première rangée.

        assertTrue(board.isFinishedMode()); // Vérifie que le jeu est terminé après un coup gagnant.
        assertEquals(Player.X, board.getWinner()); // Vérifie que le joueur X est le gagnant après un coup gagnant.
        assertEquals(Player.X, board.getCurrentTurn()); // Vérifie que le tour reste au joueur gagnant après un coup gagnant.
    }

    @Test
    public void testMarkInvalidMoveOutOfBoundsThrowsException(){
        // Test : couvre le cas où un joueur tente de marquer une case en dehors des limites du plateau.
        assertThrows(IllegalArgumentException.class, () -> {
            board.mark(-1, 0); // Tentative de marquer une case en dehors des limites (ligne négative).
        });
        assertThrows(IllegalArgumentException.class, () -> {
            board.mark(0, 3); // Tentative de marquer une case en dehors des limites (colonne supérieure à 2).
        });

    }

    @Test
    public void testMarkInvalidMoveCellAlreadySetThrowsException(){
        // Test : couvre le cas où un joueur tente de marquer une case déjà occupée.
        board.mark(0, 0); // X marque la case (0, 0).
        assertThrows(IllegalArgumentException.class, () -> {
            board.mark(0, 0); // O tente de marquer la même case (0, 0).
        });
    }

    @Test
    public void testMarkInvalidMoveAfterGameFinishedThrowsException(){
        // Test : couvre le cas où un joueur tente de marquer une case après la fin de la partie.
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(0, 2); // X gagne avec une ligne horizontale sur la première rangée.

        assertTrue(board.isFinishedMode()); // Vérifie que le jeu est terminé après un coup gagnant.
        assertEquals(Player.X, board.getWinner()); // Vérifie que le joueur X est le gagnant après un coup gagnant.

        assertThrows(IllegalArgumentException.class, () -> {
            board.mark(2, 2); // Tentative de marquer une case après la fin de la partie.
        });
    }

    @Test
    public void testRestartClearsBoardAndState(){
        // Test : couvre la méthode restart() et clearCells() pour s'assurer qu'elle réinitialise correctement l'état du plateau et le tour actuel.

        // On simule une partie gagné par X
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(0, 2); // X gagne avec une ligne horizontale sur la première rangée.

        // On relance la partie
        board.restart();

        assertTrue(board.isInProgressMode()); // Vérifie que le jeu est en cours après un redémarrage.
        assertNull(board.getWinner()); // Vérifie qu'il n'y a pas de gagnant
        assertEquals(Player.X, board.getCurrentTurn()); // Vérifie que le tour revient au joueur X après un redémarrage.

        // On vérifie que la grille est bien vide en jouant sur l'ancienne position gagnante
        board.mark(0, 2);
        assertEquals(Player.O, board.getCurrentTurn()); // Vérifie que le tour passe au joueur O après un coup valide sur une case vide.
    }

    @Test
    public void testWinVertical(){
        // Test : couvre le cas où un joueur gagne avec une ligne verticale.
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(1, 0); // X
        board.mark(1, 1); // O
        board.mark(2, 0); // X gagne avec une ligne verticale sur la première colonne.

        assertTrue(board.isFinishedMode()); // Vérifie que le jeu est terminé après un coup gagnant.
        assertEquals(Player.X, board.getWinner()); // Vérifie que le joueur X est le gagnant après un coup gagnant.
    }

    @Test
    public void testWinDiagonal(){
        // Test : couvre le cas où un joueur gagne avec une ligne diagonale.
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(1, 1); // X
        board.mark(1, 0); // O
        board.mark(2, 2); // X gagne avec une ligne diagonale.

        assertTrue(board.isFinishedMode()); // Vérifie que le jeu est terminé après un coup gagnant.
        assertEquals(Player.X, board.getWinner()); // Vérifie que le joueur X est le gagnant après un coup gagnant.
    }

    @Test
    public void testWinOppositeDiagonal(){
        // Test : couvre le cas où un joueur gagne avec une ligne diagonale opposée (haut-droite vers bas-gauche).
        board.mark(0, 2); // X
        board.mark(0, 1); // O
        board.mark(1, 1); // X
        board.mark(1, 0); // O
        board.mark(2, 0); // X gagne avec une ligne diagonale opposée.

        assertTrue(board.isFinishedMode()); // Vérifie que le jeu est terminé après un coup gagnant.
        assertEquals(Player.X, board.getWinner()); // Vérifie que le joueur X est le gagnant après un coup gagnant.
    }

    @Test
    public void testMarkDrawGame(){
        // Test : couvre le cas où le jeu se termine par un match nul, sans gagnant.

        // On remplit le plateau sans faire d'alignement (Match nul classique)
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(0, 2); // X

        board.mark(1, 1); // O
        board.mark(1, 0); // X
        board.mark(1, 2); // O

        board.mark(2, 1); // X
        board.mark(2, 0); // O

        // Le dernier coup de X qui remplit la grille
        board.mark(2, 2); // X

        assertTrue(board.isFinishedMode()); // Vérifie que le jeu est terminé après un match nul.
        assertNull(board.getWinner()); // Vérifie qu'il n'y a pas de gagnant après un match nul.
        assertTrue(board.isDraw()); // Vérifie que le jeu est reconnu comme un match nul.
    }
}