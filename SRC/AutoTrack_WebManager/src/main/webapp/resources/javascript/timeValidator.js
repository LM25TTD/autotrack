function validate(stHourId, stMinId, enHourId, enMinId, stChanged) {
    var stHour = RichFaces.$(stHourId).getValue();
    var stMin = RichFaces.$(stMinId).getValue();
    var enHour = RichFaces.$(enHourId).getValue();
    var enMin = RichFaces.$(enMinId).getValue();
    if (stChanged) {
        if (enHour < stHour) {
            enHour = stHour;
        } else if (enHour == stHour && enMin < stMin) {
            enMin = stMin;
        }
    } else {
        if (enHour < stHour) {
            stHour = enHour;
        } else if (enHour == stHour && enMin < stMin) {
            stMin = enMin;
        }
    }
    RichFaces.$(stHourId).setValue(stHour);
    RichFaces.$(stMinId).setValue(stMin);
    RichFaces.$(enHourId).setValue(enHour);
    RichFaces.$(enMinId).setValue(enMin);
}
