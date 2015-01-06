package gavin;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GavinJuniorBotTest extends GavinJuniorBot {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMinAngle30() {
		assertEquals(30, minTurnAngle(30), 0);
	}
	
	@Test
	public void testMinAngle230() {
		assertEquals(-130, minTurnAngle(230), 0);
	}
	
	@Test
	public void testMinAngle430() {
		assertEquals(70, minTurnAngle(430), 0);
	}
	
	@Test
	public void testMinAngle730() {
		assertEquals(10, minTurnAngle(730), 0);
	}
	
	@Test
	public void testMinAngleMinus30() {
		assertEquals(-30, minTurnAngle(-30), 0);
	}
	
	@Test
	public void testMinAngleMinus190() {
		assertEquals(170, minTurnAngle(-190), 0);
	}
	
	@Test
	public void testMinAngleMinus390() {
		assertEquals(-30, minTurnAngle(-390), 0);
	}
	
	@Test
	public void testMinAngleMinus790() {
		assertEquals(-70, minTurnAngle(-790), 0);
	}
	
	@Test
	public void testMinAngle180() {
		assertEquals(180, minTurnAngle(180), 0);
	}

}
