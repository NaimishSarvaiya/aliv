package com.iotsmartaliv.utils;

public class ErrorMsgDoorMasterSDK {
    public static String getErrorMsg(int errorCOde) {
        switch (errorCOde) {
            case 1:
                return "CRC check error";
            case 2:
                return "Communication command format error";
            case 3:
                return "Administration password error";
            case 4:
                return "ERROR_POWER";
            case 5:
                return "Data read and write error";
            case 6:
                return "Un - registered user in device";
            case 7:
                return "Random number detection error";
            case 8:
                return " Random number error";
            case 9:
                return "Command length is not matched";
            case 10:
                return "Not enter into add device mode";
            case 11:
                return "devKey detection error";
            case 12:
                return "Function not support";
            case 13:
                return "Equipment capacity deficiency";


            case 48:
                return "Communication connection timeout";
            case 49:
                return "Bluetooth sevice is not found";
            case 50:
                return "Communication data length error, append again";
            case 51:
                return "Empty accepted data";
            case 52:
                return "Command parsing error";
            case 53:
                return "Not obtain random number";
            case 54:
                return "Not obtain configuration subcommand";
            case 55:
                return "Not obtain data operation subcommand";


            case -1:
                return "Cardno is null";
            case -2:
                return "devSn is null";
            case -3:
                return "devMac is null";
            case -4:
                return "eKey is null";
            case -5:
                return "Device type is null";
            case -6:
                return "Device right is null";
            case -7:
                return "Open Type in device is not empty";
            case -8:
                return "Device verification method is null";
            case -9:
                return "Starting time format error";
            case -10:
                return "Terminate time format error";
            case -11:
                return "Use time not set";
            case -12:
                return "The value is undefined";
            case -13:
                return "Operation other functions are not open";
            case -14:
                return "Illegal time to open the door, that is not open during the validity of the error";
            case -15:
                return "More than the set door distance";
            case -16:
                return "Wiegand format error, currently only supports 26 and 34";
            case -17:
                return "Open the door length range is wrong, only support 1 - 254 seconds";
            case -18:
                return "Electrical switch parameter value is wrong, only 0 electric lock control, 1 electrical switch";
            case -19:
                return "The password must be 6 digits";
            case -20:
                return "The card number list can not be empty";
            case -21:
                return "Card number written to the device, each can not be greater than 200 cards";
            case -22:
                return "The sector key must be a hexadecimal string and a length of 12";
            case -23:
                return "The device number range can only be 0 - 255";
            case -24:
                return "The card sector number can only be 0 - 15";
            case -25:
                return "The scanTime parameter can not be empty";
            case -41:
                return "Device can not be null";
            case -42:
                return "Context can not be null";
            case -43:
                return "Device expires";
            case -44:
                return "Device is not in use";
            case -100:
                return  "Open door Failure";
                // Old Message
//                return "Does not support BLE";
            case -101:
                return "BLE is not open";
            case -102:
                return "The specified SN does not exist";
            case -103:
                return "Empty value for bluetooth communication return";
            case -104:
                return "Open the door to failure";
            case -105:
                return "The device is not responding";
            case -106:
                return "The equipment is not nearby";
            case -107:
                return "Device is under operating";
            case -108:
                return "Sec Scan time unit error";
            case -109:
                return "Scan seconds set is out of range";
            case -110:
                return "The device already has a superuser, and the device must be initialized to add the device";
            case -111:
                return "Device MAC address is incorrect";
            case -112:
                return "Bluetooth scan too frequent Please Wait for moment and try again";
//                        "Bluetooth scan too frequent Please Wait and try again(Android 7.0 limitation)";
            default:
                return "Unknown ERROR " + errorCOde;
        }
    }
}
