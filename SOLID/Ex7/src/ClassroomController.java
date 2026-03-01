public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) { this.reg = reg; }

    public void startClass() {
        PowerControl pjPower = reg.getFirstOfType("Projector", PowerControl.class);
        pjPower.powerOn();
        InputConnect pjInput = reg.getFirstOfType("Projector", InputConnect.class);
        pjInput.connectInput("HDMI-1");

        BrightnessControl lights = reg.getFirstOfType("LightsPanel", BrightnessControl.class);
        lights.setBrightness(60);

        TemperatureControl ac = reg.getFirstOfType("AirConditioner", TemperatureControl.class);
        ac.setTemperatureC(24);

        AttendanceScan scan = reg.getFirstOfType("AttendanceScanner", AttendanceScan.class);
        System.out.println("Attendance scanned: present=" + scan.scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        reg.getFirstOfType("Projector", PowerControl.class).powerOff();
        reg.getFirstOfType("LightsPanel", PowerControl.class).powerOff();
        reg.getFirstOfType("AirConditioner", PowerControl.class).powerOff();
    }
}