package fr.ekode.fabriclockette.core.lang;

import fr.ekode.fabriclockette.core.Config;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LangTest {
    @TempDir
    static Path sharedTempDir;

    private Lang lang;

    @BeforeEach
    void setUp() throws IOException {
        Path configPath = sharedTempDir.resolve("config.properties");
        Config.initInstance(configPath);
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