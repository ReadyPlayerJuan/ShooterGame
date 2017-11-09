<?xml version="1.0" encoding="UTF-8"?>
<tileset name="testTileSet" tilewidth="16" tileheight="16" spacing="1" margin="1" tilecount="132" columns="12">
 <image source="../res/tileset.png" trans="ff00ff" width="205" height="188"/>
 <terraintypes>
  <terrain name="Grass" tile="12"/>
  <terrain name="Bright Grass" tile="45"/>
  <terrain name="Water" tile="106"/>
  <terrain name="Sand" tile="27"/>
 </terraintypes>
 <tile id="0" terrain="3,3,3,0"/>
 <tile id="1" terrain="3,3,0,0"/>
 <tile id="2" terrain="3,3,0,3"/>
 <tile id="3" terrain="0,0,0,3"/>
 <tile id="4" terrain="0,0,3,0"/>
 <tile id="12" terrain="3,0,3,0"/>
 <tile id="13" terrain="0,0,0,0"/>
 <tile id="14" terrain="0,3,0,3"/>
 <tile id="15" terrain="0,3,0,0"/>
 <tile id="16" terrain="3,0,0,0"/>
 <tile id="21">
  <objectgroup draworder="index">
   <object id="1" x="2" y="4" width="12" height="10">
    <properties>
     <property name="direction" value="urdl"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="24" terrain="3,0,3,3"/>
 <tile id="25" terrain="0,0,3,3"/>
 <tile id="26" terrain="0,3,3,3"/>
 <tile id="27" terrain="1,1,1,0"/>
 <tile id="28" terrain="1,1,0,1"/>
 <tile id="29" terrain="3,3,3,3"/>
 <tile id="34">
  <objectgroup draworder="index">
   <object id="1" x="2" y="4" width="12" height="10">
    <properties>
     <property name="direction" value="urdl"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="36" terrain="0,0,0,1"/>
 <tile id="37" terrain="0,0,1,1"/>
 <tile id="38" terrain="0,0,1,0"/>
 <tile id="39" terrain="1,0,1,1"/>
 <tile id="40" terrain="0,1,1,1"/>
 <tile id="48" terrain="0,1,0,1"/>
 <tile id="49" terrain="1,1,1,1"/>
 <tile id="50" terrain="1,0,1,0"/>
 <tile id="60" terrain="0,1,0,0"/>
 <tile id="61" terrain="1,1,0,0"/>
 <tile id="62" terrain="1,0,0,0"/>
 <tile id="72" terrain=",,,0"/>
 <tile id="73" terrain=",,0,0"/>
 <tile id="74" terrain=",,0,"/>
 <tile id="75" terrain=",,,3"/>
 <tile id="76" terrain=",,3,3"/>
 <tile id="77" terrain=",,3,"/>
 <tile id="84" terrain=",0,,0"/>
 <tile id="86" terrain="0,,0,"/>
 <tile id="87" terrain=",3,,3"/>
 <tile id="89" terrain="3,,3,"/>
 <tile id="93" terrain="2,2,2,0">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="12" height="16">
    <properties>
     <property name="direction" value="r"/>
    </properties>
   </object>
   <object id="2" x="0" y="0" width="16" height="8">
    <properties>
     <property name="direction" value="d"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="94" terrain="2,2,0,2">
  <objectgroup draworder="index">
   <object id="1" x="4" y="0" width="12" height="16">
    <properties>
     <property name="direction" value="l"/>
    </properties>
   </object>
   <object id="2" x="0" y="0" width="16" height="8">
    <properties>
     <property name="direction" value="d"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="96" terrain=",0,,"/>
 <tile id="97" terrain="0,0,,"/>
 <tile id="98" terrain="0,,,"/>
 <tile id="99" terrain=",3,,"/>
 <tile id="100" terrain="3,3,,"/>
 <tile id="101" terrain="3,,,"/>
 <tile id="102" terrain="0,0,0,2">
  <objectgroup draworder="index">
   <object id="1" x="4" y="0" width="12" height="16">
    <properties>
     <property name="direction" value="lu"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="103" terrain="0,0,2,2">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="16">
    <properties>
     <property name="direction" value="u"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="104" terrain="0,0,2,0">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="12" height="16">
    <properties>
     <property name="direction" value="ur"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="105" terrain="2,0,2,2">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="16">
    <properties>
     <property name="direction" value="ur"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="106" terrain="0,2,2,2">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="16">
    <properties>
     <property name="direction" value="ul"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="114" terrain="0,2,0,2">
  <objectgroup draworder="index">
   <object id="1" x="4" y="0" width="12" height="16">
    <properties>
     <property name="direction" value="l"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="115" terrain="2,2,2,2">
  <objectgroup draworder="index"/>
 </tile>
 <tile id="116" terrain="2,0,2,0">
  <objectgroup draworder="index">
   <object id="4" x="0" y="0" width="12" height="16">
    <properties>
     <property name="direction" value="r"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="126" terrain="0,2,0,0">
  <objectgroup draworder="index">
   <object id="1" x="4" y="0" width="12" height="8">
    <properties>
     <property name="direction" value="ld"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="127" terrain="2,2,0,0">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="8">
    <properties>
     <property name="direction" value="d"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="128" terrain="2,0,0,0">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="12" height="8">
    <properties>
     <property name="direction" value="dr"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>
