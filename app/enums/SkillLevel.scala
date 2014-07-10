package enums

//Dreyfus model of skill acquisition
//    1. Novice
//        "rigid adherence to taught rules or plans"
//        no exercise of "discretionary judgment"
//    2. Advanced beginner
//        limited "situational perception"
//        all aspects of work treated separately with equal importance
//    3. Competent
//        "coping with crowdedness" (multiple activities, accumulation of information)
//        some perception of actions in relation to goals
//        deliberate planning
//        formulates routines
//    4. Proficient
//        holistic view of situation
//        prioritizes importance of aspects
//        "perceives deviations from the normal pattern"
//        employs maxims for guidance, with meanings that adapt to the situation at hand
//    5. Expert
//        transcends reliance on rules, guidelines, and maxims
//        "intuitive grasp of situations based on deep, tacit understanding"
//        has "vision of what is possible"
//        uses "analytical approaches" in new situations or in case of problems

object SkillLevel extends BaseEnumeration {
  type SkillLevel = Value
  val NOVICE, ADVANCEDBEGINNER, COMPETENT, PROFICIENT, EXPERT = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(SkillLevel)
}

