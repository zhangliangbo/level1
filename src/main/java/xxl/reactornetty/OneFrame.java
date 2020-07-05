package xxl.reactornetty;

import java.io.IOException;
import java.nio.ByteBuffer;

public class OneFrame {
    public static void main(String[] args) throws IOException {
//        byte[] bytes = IOUtils.toByteArray(new FileInputStream("C:\\Users\\Admin\\Desktop\\frame.txt"));
        byte[] bytes = "AA55\u0001   \u0002�<?xml version=\"1.0\" encoding=\"utf-8\"?><TrackReport><Track Reported=\"2020-07-03T18:37:06.165\" Id=\"152268\" DbId=\"d5f281a0-7792-4ce9-91c4-6a70ccb5ca99\" SizeInAz=\"4.01\" SizeInRange=\"0.45\" Seen=\"13\" Coasts=\"3\"><Location X=\"2941.08\" Y=\"4530.50\" Z=\"0.0\" DirectionAngle=\"64.92965871349473\" DirectionDegs=\"295.44\" Speed=\"5.5\" LaneUserId=\"0\" SectionUserId=\"87\" CarriagewayName=\"Carriageway 15\"/><Status ThreatLevel=\"Threat\" BrokenRules=\"26dfd571-c254-4357-8694-d5a9d2c6db18\" Classification=\"Unclassified\" ClassificationProbability=\"0.0\"/><GeoData Latitude=\"30.41956316\" Longitude=\"114.14776241\"/><Radar RadarId=\"15\" Name=\"设法山路(两军西路)路口西进口电子警察杆\"/></Track></TrackReport>AA55\u0001   \u0002�<?xml version=\"1.0\" encoding=\"utf-8\"?><TrackReport><Track Reported=\"2020-07-03T18:37:06.165\" Id=\"76226\" DbId=\"2cecc175-fbe6-4b66-8676-866ff44a8c1a\" SizeInAz=\"10.69\" SizeInRange=\"1.35\" Seen=\"45\" Coasts=\"0\"><Location X=\"3735.87\" Y=\"4183.10\" Z=\"0.0\" DirectionAngle=\"288.7567150224167\" DirectionDegs=\"119.58\" Speed=".getBytes();
        System.err.println("len:" + bytes.length);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        byte[] head = new byte[4];
        buffer.get(head);
        System.err.println(new String(head));
        System.err.println(buffer.get());
        System.err.println(buffer.get());
        System.err.println(buffer.getInt());
    }
}
