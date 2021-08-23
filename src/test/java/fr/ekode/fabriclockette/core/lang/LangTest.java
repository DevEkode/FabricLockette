package fr.ekode.fabriclockette.core.lang;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LangTest {

    private Lang lang;

    @BeforeEach
    void setUp() {
        this.lang = Lang.getINSTANCE();
    }

    @Test
    void withKey() {
        Text expected = new LiteralText("This block is protected");
        Text text = this.lang.withKey("block.fabriclockette.denied");
        assertEquals(expected,text);
    }

    @Test
    void withKeyUnknown() {
        Text expected = new LiteralText("translation not found");
        Text text = this.lang.withKey("block.fabriclockette.thisdoesnotexists");
        assertEquals(expected,text);
    }
}