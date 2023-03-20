import org.junit.Assert.assertTrue
import org.junit.Test

class UnitTest {
    //if need to run from top to bottom, need to rename those function in A-Z order

//    @Test
//    //if after run, green tick -> pass the test
//    //red tick -> don't pass the test
//    fun test1(){
//        heroes.clear()
//        monarchHero = CaoCao()
//        heroes.add(monarchHero)
//        heroes.add(ZhangFei(MinisterRole()))
//        assertTrue(monarchHero.name == "Cao Cao")
//    }
//
//    @Test
//    fun test2(){
//        //if run through all will help no problem
//        //if want to run this code independently, may need to call other fast
//        test1()
//        assertTrue(heroes.size == 2)
//    }
//
//    @Test
//    fun testLiuBeiName(){
//        //before run code below, it will keep the setting before
//        monarchHero = LiuBei()
//        assertTrue(monarchHero.name == "Liu Bei")
//    }

    @Test
    fun testCaoDodgeAttack() {
        monarchHero = CaoCao()
        for (n in 0..6) {
            heroes.add(NoneMonarchFactory.createRandomHero())
        }
        assertTrue(monarchHero.dodgeAttack())
    }

    @Test
    fun testBeingAttacked() {
        if (heroes.isEmpty())
            heroes.add(NoneMonarchFactory.createRandomHero())

        for (hero in heroes) {
            val spy = object : Hero(MinisterRole()) {
                override val name = hero.name

                override fun beingAttacked() {
                    hero.beingAttacked()
                    assertTrue(hero.hp >= 0)
                }
            }
            for (i in 0 until 10) {
                spy.beingAttacked()
            }
        }
    }

    @Test
    fun testDiscardCards(){
        val dummy = DummyRole()
        val hero = ZhangFei(dummy)

        hero.discardCards()
    }
}

object FakeNonmonarchFactory : GameObjectFactory {
    var count = 0
    var last: WeiHero? = null

    fun createRandomRole(): Role {
        return MinisterRole()
    }

    override fun getRandomRole(): Role {
        return MinisterRole()
    }

    override fun createRandomHero(): Hero {
        val hero = when (count++) {
            0 -> SimaYi(createRandomRole())
            1 -> XuChu(createRandomRole())
            else -> XiaHouyuan(createRandomRole())
        }
        if (count > 3) {
            count = 0
        }
        val cao = monarchHero as CaoCao
        if (last == null)
            cao.helper = hero
        else
            last!!.setNext(hero)
        last = hero
        return hero
    }
}

object FakeMonarchFactory : GameObjectFactory {
    override fun createRandomHero(): Hero {
        return CaoCao()
    }

    override fun getRandomRole(): Role {
        return MonarchRole()
    }
}

class CaoCaoUnitTest() {
    @Test
    fun testCaoDodgeAttack() {
        monarchHero = FakeMonarchFactory.createRandomHero() as MonarchHero
        heroes.add(monarchHero)
        monarchHero.setCommand(Abandon(monarchHero))
        for (i in 0..2) {
            var hero = FakeNonmonarchFactory.createRandomHero()
            hero.index = heroes.size;
            heroes.add(hero)
        }

        for (hero in heroes) {
            hero.beingAttacked()
            hero.templateMethod()
        }
        assertTrue(monarchHero.dodgeAttack())
    }
}

class DummyRole(): Role{
    override val roleTitle = "Dummy"

    override fun getEnemy(): String {
        TODO("Not yet implemented")
    }
}