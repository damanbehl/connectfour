package game;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ConnectFourDriverTest 
{
    /**
     * Rigorous Test :-)
     */
    static int testCount = 1;
    @Before // To indicate that a function should run before each test.
    public void setThingsUp(){
        System.out.println("Preparing test #" + testCount + " in OtherExampleUnitTest");
    }
    
    @Test
    public void testIsWinner(){
        char player = 'R';
        //initialize a grid where we already know the outcome
        char grid[][] = new char[][]{{' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ','R',' ',' ',' '},
        {' ',' ',' ','R',' ',' ',' '},
        {'B',' ',' ','R',' ',' ',' '},
        {'B','B',' ','R',' ',' ',' '}};
        boolean result = ConnectFourDriver.isWinner(player, grid);
        assertTrue( result );
        // assertFalse("the player did not win", ConnectFourDriver.isWinner('R', grid));
    }
    @Test
    public void testValidation(){
        char grid[][] = new char[][]{{' ',' ',' ','R',' ',' ',' '},
                                {' ',' ',' ','R',' ',' ',' '},
                                {' ',' ',' ','B',' ',' ',' '},
                                {' ',' ',' ','R',' ',' ',' '},
                                {'B',' ',' ','R',' ',' ',' '},
                                {'B','B','B','R',' ',' ',' '}};
        //validate input
        boolean validInput = ConnectFourDriver.validate(1, grid);
        System.out.println(validInput);
        // assertFalse(validInput);
        assertTrue(validInput);
        
    }
    
    @Test
    public void testValidatLocations(){
        char grid[][] = new char[][]{{' ',' ',' ','R',' ',' ',' '},
                                    {' ',' ',' ','R',' ',' ',' '},
                                    {' ',' ',' ','B',' ',' ',' '},
                                    {' ',' ',' ','R',' ',' ',' '},
                                    {'B',' ',' ','R',' ',' ',' '},
                                    {'B','B','B','R',' ',' ',' '}};
        int locations[] = ConnectFourDriver.getValidLocation(grid);
        int results[] = {0, 1, 2, 4, 5, 6};
        System.out.println(Arrays.toString(locations));
        assertArrayEquals(locations, results);
    }

    @Test
    public void testDropPiece(){
        char grid[][] = new char[][]{{' ',' ',' ',' ',' ',' ',' '},
                                     {' ',' ',' ','R',' ',' ',' '},
                                     {' ',' ',' ','B',' ',' ',' '},
                                     {' ',' ',' ','R',' ',' ',' '},
                                     {' ',' ',' ','R',' ',' ',' '},
                                     {' ','B','B','R',' ',' ',' '}};
        char resultantGrid[][] = new char[][]{{' ',' ',' ','B',' ',' ',' '},
                                              {' ',' ',' ','R',' ',' ',' '},
                                              {' ',' ',' ','B',' ',' ',' '},
                                              {' ',' ',' ','R',' ',' ',' '},
                                              {' ',' ',' ','R',' ',' ',' '},
                                              {' ','B','B','R',' ',' ',' '}};
        char returnedGrid[][] = ConnectFourDriver.dropPiece(grid, 3, 'B'); 
        for(int i=0;i<resultantGrid.length;i++){
            System.out.println("comparing grid index"+i);
            assertArrayEquals(resultantGrid[i], returnedGrid[i]);
            assertTrue("in the grid. row no." + i +
            " in expected and actual aren't the same.",
                       Arrays.equals(resultantGrid[i],  returnedGrid[i]));
        }
        
        
    }
    @Test
    public void testGetDiagonal(){
        char grid[][] = new char[][]{{' ',' ',' ',' ',' ',' ',' '},
                                     {' ',' ',' ','R',' ',' ',' '},
                                     {' ',' ',' ','B','R',' ',' '},
                                     {' ',' ',' ','R',' ','R',' '},
                                     {' ',' ',' ','R',' ',' ','R'},
                                     {' ','B','B','R',' ',' ',' '}};
        char expecteds[] = {'R','R','R','R'};
        char actuals[] = ConnectFourDriver.getDiagonal(grid, 1, 3);
        assertArrayEquals(expecteds, actuals);
    }   

    @After // To indicate that a function should run after each test.
    public void cleanThingsUp(){
        System.out.println("Cleaning after test #" + testCount + " in ExampleUnitTest");
        ++testCount;
    }
}
