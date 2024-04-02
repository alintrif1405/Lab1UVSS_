userId = 1
        function loadCourses() {
            fetch(`/${userId}/courses`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(coursesData => {
                    const obligatoryCourses = coursesData.filter(course => course.isObligatory);
                    const optionalCourses = coursesData.filter(course => !course.isObligatory);

                    const createCourseDiv = course => {
                        const courseDiv = document.createElement('div');
                        courseDiv.classList.add('course-window');
                        courseDiv.innerHTML = `
                        <div class="blue-ribbon"></div>
                        <div class="course-details">
                            <div class="title">${course.courseName}</div>
                            <div class="subtitle">${course.abbreviation}</div>
                            <div class="text">${course.professor}</div>
                            <div class="text">${course.creditNr}</div>
                        </div>
                    `;
                        return courseDiv;
                    };

                    const addCoursesToContainer = (courses, containerId) => {
                        const courseContainer = document.getElementById(containerId);
                        courseContainer.innerHTML = '';
                        courses.forEach(course => {
                            const courseDiv = createCourseDiv(course);
                            courseContainer.appendChild(courseDiv);
                        });
                    };

                    addCoursesToContainer(obligatoryCourses, 'obligatoryCoursesContainer');

                    addCoursesToContainer(optionalCourses, 'optionalCoursesContainer');
                })
                .catch(error => console.error('Error fetching courses:', error));
        }

        document.addEventListener('DOMContentLoaded', loadCourses);
