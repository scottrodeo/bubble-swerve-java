package rodeo.scott.bubbleswerve;

/**
 * Represents the possible movement directions for game pieces in the Bubble Swerve game.
 * These directions are used to define and handle movements and actions in the game grid.
 * 
 * Enum constants:
 * - `LEFT`: Move the piece one column to the left.
 * - `RIGHT`: Move the piece one column to the right.
 * - `DOWN`: Move the piece one row downward.
 * - `DROP`: Instantly drop the piece to the lowest possible position.
 * - `UP`: Move the piece one row upward (if applicable).
 * 
 * @author https://scott.rodeo/
 */
public enum Direction {
    LEFT,   // Move one column to the left.
    RIGHT,  // Move one column to the right.
    DOWN,   // Move one row downward.
    DROP,   // Drop to the lowest valid position immediately.
    UP      // Move one row upward.
}
