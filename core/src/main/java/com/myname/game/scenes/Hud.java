package com.myname.game.scenes;


//UI works with pixel not meters

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myname.game.utils.Constants;

public class Hud {

    //UI management
    private Stage stage;
    private Viewport viewport;

    //Visual style
    private Skin skin;
    private Table dialogTable;
    private Label dialogLable;

    public Hud(SpriteBatch batch)
    {
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        //Gdx.input.setInputProcessor(stage);   buton tiklamalari icin lazim !!!

        createBasicSkin();
        createDialogBox();
    }

    public void draw()
    {
        //Hud camera always static
        stage.getViewport().apply();

        //Stage did its own draw
        stage.act();
        stage.draw();
    }

    public void showDialog(String text)
    {
        dialogLable.setText(text);
        dialogTable.setVisible(true);
    }

    public void hideDialog()
    {
        dialogTable.setVisible(false);
    }

    public boolean isDialogVisible()
    {
        return dialogTable.isVisible();
    }

    public void resize(int width, int height)
    {
        viewport.update(width, height, true); // true: Kamerayı ortala

        // Eğer tablo oluşturulmuşsa, genişliğini yeni ekran genişliğine eşitle
        if(dialogTable != null) {
            dialogTable.setWidth(stage.getWidth());
        }
    }


    private void createBasicSkin() {
        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/font.ttf")); // Font dosyanın adı neyse
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // 1. BOYUT KURALI (Çok Önemli!)
        // İndirdiğin Pixel Fontun "orijinal boyutu" neyse (genelde 16px veya 32px olur),
        // buraya onun katlarını yazmalısın. (16, 32, 48 gibi).
        // Ara değerler (örn: 20px) pixel art fontu bozar ve yamuk gösterir.
        parameter.size = 16;

        // 2. SİHİRLİ AYARLAR (Bulanıklığı Yok Eden Kısım)
        // "Linear" yumuşatır, "Nearest" keskin bırakır.
        parameter.minFilter = Texture.TextureFilter.Nearest; // Küçülürken keskin kalsın
        parameter.magFilter = Texture.TextureFilter.Nearest; // Büyürken keskin kalsın

        // Ekstra Garanti: Gölgelendirmeyi kapat (Bazen varsayılan açık olabilir)
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;

        // Türkçe karakter desteği
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ığüşöçİĞÜŞÖÇ";

        // Fontu oluştur
        BitmapFont font = generator.generateFont(parameter);

        // 3. FONTU TEMİZLEME (Önemli)
        // Bazen oluşturduktan sonra bile scale edilirken bozulabilir.
        // Bunu engellemek için oluşan fontun kendi filtresini de zorluyoruz.
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Scale (ölçek) bozulmasını engelle (Her zaman 1.0 olsun)
        font.getData().setScale(1.0f);

        generator.dispose();

        skin.add("default-font", font);

        // ... Diğer kısımlar (White texture vs.) aynı kalsın ...
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);
    }

    private void createDialogBox()
    {
        // 1. Tabloyu oluştur
        dialogTable = new Table();

        // 2. KONUM VE BOYUT (Hayati Kısım)
        dialogTable.setPosition(0, 0); // Ekranın sol alt köşesine çivile
        dialogTable.setWidth(stage.getWidth()); // Genişliği ekran kadar olsun
        dialogTable.setHeight(150); // YÜKSEKLİĞİ ELLE VERİYORUZ! (Burası 0 olduğu için görünmüyordu)

        // 3. İçerik Hizalaması
        // Tablonun içindeki yazı sol-üstten başlasın
        dialogTable.left().top();

        // 4. Arka Plan
        TextureRegionDrawable background = new TextureRegionDrawable(skin.getRegion("white"));
        dialogTable.setBackground(background.tint(new Color(0, 0, 0, 0.8f)));

        // 5. Label (Yazı)
        dialogLable = new Label("", skin);
        dialogLable.setWrap(true); // Yazı taşarsa alt satıra geç
        dialogLable.setAlignment(Align.topLeft);

        // 6. Tabloya Ekleme
        // pad: Kenarlardan boşluk bırakır
        // expandX: Yatayda alanı doldur
        // fillX: Label'ı yatayda sündür
        dialogTable.add(dialogLable).expandX().fillX().pad(20);

        // Başlangıçta gizli
        dialogTable.setVisible(false);

        // Sahneye ekle
        stage.addActor(dialogTable);
    }

}
