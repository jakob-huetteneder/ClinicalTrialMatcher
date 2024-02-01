# Clinical Trial Matcher
This project was developed as part of a university course on software engineering and project management at TU Wien. It addresses the problem of finding suitable participants for specialized medical trials. Pharmaceutical companies as well as hospitals and medical universities develop new medication and technologies to enhance the state-of-the-art treatment possibilities in the medical sector. However, in order to ensure the efficacy and safety of these inventions as well as to conform with the regulatory bodies of local governments, these companies have to engage in rigorous testing for which they need participants. The requirements on these participants are diverse and are highly dependent on the specific drug or procedure tested. To make it easier to find, apply and complete these drugs for both parties (companies and participants) we have developed **Clinical Trial Matcher**.

Clinical Trial Matcher allows people interested in participating in a trial to create an account and add search a vast database of clinical trials using sophisticated filtering to confine the selection to suitable ones. It is also possible to connect to doctors, which the user trusts, to further ensure a match between the person's personal medical history and possible studies. Furthermore, it is possible to add one's own medical history and imagery to be utilized in the search of appropriate trials.

On the other side, study labs and pharmaceutical companies have the option to publish their clinical trials and view statistical analysis of the registered and requested participants. This helps to keep an overview on possibly required advertisement or compensation to successfully fulfill regulatory requirements.

## Named Entity Recognition on Medical History
We employ an advanced NER model to be able to scan notes from the medical history for relevant information, making the process of categorically matching patient data to clinical trials easier. For this, not only prior diagnoses but also medication are taken into account. A special focus is put on the recognition of negations, as many medical documents mention keywords only to describe their non-problematic state. 

## Medical Information Retrieval
To find the best match with clinical trials for every patient, we employ state-of-the-art text retrieval techniques to account for the specific demands of the medical domain.

## Technologies
The software consists of a separate frontend and backend server. The frontend is written using Angular with Typescript. For the backend, we have used Java with Spring Boot. Finally, there is also another submodule containing the retrieval specific logic written in Python using several specialized libraries. For further information on the setup of the components, you can view the descriptions provided in the respective components.
