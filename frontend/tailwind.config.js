/** @type {import('tailwindcss').Config} */
module.exports = {
  prefix: '',
  purge: {
    content: [
      './src/**/*.{html,ts}',
    ]
  },
  darkMode: 'class', // or 'media' or 'class'
  theme: {
    extend: {
      colors:{
        'cool-blue': {
          500: '#308BC8',
        },
      }
    },
  },
  variants: {
    extend: {},
  },
  plugins: [require('@tailwindcss/forms'),require('@tailwindcss/typography')],
};

